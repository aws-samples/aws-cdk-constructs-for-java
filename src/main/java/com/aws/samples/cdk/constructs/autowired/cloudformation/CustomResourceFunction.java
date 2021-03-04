package com.aws.samples.cdk.constructs.autowired.cloudformation;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.aws.samples.cdk.constructs.iam.permissions.HasIamPermissions;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.NoSuchElementException;

import static com.awslabs.general.helpers.implementations.IoHelper.toByteArray;
import static com.awslabs.general.helpers.implementations.JsonHelper.fromJson;
import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

public abstract class CustomResourceFunction implements RequestStreamHandler, HasIamPermissions {
    private final CustomResourceResponseSender customResourceResponseSender = new CustomResourceResponseSender();

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) {
        CustomResourceRequest customResourceRequest = fromJson(CustomResourceRequest.class, toByteArray(inputStream));

        handleRequest(customResourceRequest, context);
    }

    public void handleRequest(CustomResourceRequest customResourceRequest, Context context) {
        Try<CustomResourceResponse> responseTry = Match(customResourceRequest.getRequestType()).of(
                Case($(CustomResourceRequestType.CREATE), () -> Try.of(() -> create(customResourceRequest, context))),
                Case($(CustomResourceRequestType.DELETE), () -> Try.of(() -> delete(customResourceRequest, context))),
                Case($(CustomResourceRequestType.UPDATE), () -> Try.of(() -> update(customResourceRequest, context))),
                Case($(), type -> Try.failure(new UnsupportedOperationException("Unknown request type [" + type + "]"))));

        responseTry
                // Attempt to convert any failures into a failure response we can send back
                .recoverWith(throwable -> createFailureResponse(throwable, customResourceRequest, context))
                // If the conversion fails just log it
                .onFailure(throwable -> context.getLogger().log("Failed to convert a failure response to the correct format [" + throwable.getMessage() + "]"))
                // Try to send the response
                .map(customResourceResponseSender::send)
                // Verify sendResponse is true so we know it was sent successfully
                .filter(sendResponse -> sendResponse)
                // If the sender returns false then sending the response failed
                .getOrElseThrow(() -> new RuntimeException("Failed to send the response to the CloudFormation service"));
    }

    private Try<CustomResourceResponse> createFailureResponse(Throwable throwable, CustomResourceRequest customResourceRequest, Context context) {
        return Try.of(() -> ImmutableCustomResourceResponse.builder()
                .customResourceRequest(customResourceRequest)
                .status(CustomResourceStatus.FAILED)
                .reason(throwable.getMessage())
                .context(context)
                .build());
    }

    protected abstract CustomResourceResponse update(CustomResourceRequest cfnRequest, Context context);

    protected abstract CustomResourceResponse delete(CustomResourceRequest cfnRequest, Context context);

    protected abstract CustomResourceResponse create(CustomResourceRequest cfnRequest, Context context);

    /**
     * Convenience function to return SUCCESS to a delete request
     *
     * @param customResourceRequest
     * @param context
     * @return
     */
    protected CustomResourceResponse simpleDeleteSuccess(CustomResourceRequest customResourceRequest, Context context) {
        return ImmutableCustomResourceResponse.builder()
                .customResourceRequest(customResourceRequest)
                .context(context)
                .status(CustomResourceStatus.SUCCESS)
                // In a delete request the physical resource ID must be present
                .physicalResourceId(customResourceRequest.getPhysicalResourceId().get())
                .build();
    }

    /**
     * Convenience function to return SUCCESS to a create or update request
     *
     * @param customResourceRequest
     * @param context
     * @return
     */
    protected CustomResourceResponse simpleCreateOrUpdateSuccess(CustomResourceRequest customResourceRequest, Context context, String newPhysicalResourceId) {
        return ImmutableCustomResourceResponse.builder()
                .customResourceRequest(customResourceRequest)
                .context(context)
                .status(CustomResourceStatus.SUCCESS)
                .physicalResourceId(newPhysicalResourceId)
                .build();
    }

    protected <T> Try<T> tryGetValue(CustomResourceRequest customResourceRequest, String key, Class<T> clazz) {
        // Attempt to get the requested key
        return Option.of(customResourceRequest.getResourceProperties().get(key))
                // Convert the result to a try (NoSuchElementException if is missing)
                .toTry()
                // If the key is missing give a more clear error message about what is wrong
                .mapFailure(Case($(instanceOf(NoSuchElementException.class)), new RuntimeException("Key [" + key + "] was not found in the custom resource request")))
                // If the key is present try to cast it to the requested class
                .map(clazz::cast)
                // Get the value or throw a clear exception about the cast failing
                .mapFailure(Case($(instanceOf(ClassCastException.class)), new RuntimeException("Key [" + key + "] was found in the custom resource request but could not be cast to a [" + clazz.getName() + "]")));
    }
}

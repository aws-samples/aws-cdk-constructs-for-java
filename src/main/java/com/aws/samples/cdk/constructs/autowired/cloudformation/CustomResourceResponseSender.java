package com.aws.samples.cdk.constructs.autowired.cloudformation;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import io.vavr.control.Try;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import static com.awslabs.general.helpers.implementations.JsonHelper.toJson;

public class CustomResourceResponseSender {
    private final HttpClient httpClient = HttpClientBuilder.create().build();

    public <T> boolean send(CustomResourceResponse customResourceResponse) {
        Context context = customResourceResponse.getContext();
        LambdaLogger logger = context.getLogger();
        String body = toJson(customResourceResponse);
        StringEntity bodyStringEntity = Try.of(() -> new StringEntity(body)).getOrElseThrow(() -> new RuntimeException("Could not convert body to a StringEntity"));

        HttpPut httpPut = new HttpPut(customResourceResponse.getCustomResourceRequest().getResponseURL());
        httpPut.setEntity(bodyStringEntity);

        // Make the HTTPS request
        return Try.of(() -> httpClient.execute(httpPut, new BasicResponseHandler()))
                // Log if it fails
                .onFailure(exception -> logger.log("Sending the custom resource response failed [" + exception.getMessage() + "]"))
                // Return to the caller whether it was successful or not
                .isSuccess();
    }
}

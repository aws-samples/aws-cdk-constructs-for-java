package com.aws.samples.cdk.constructs.autowired.cloudformation;

import com.amazonaws.services.lambda.runtime.Context;
import com.aws.samples.cdk.constructs.iam.permissions.sqs.actions.GetQueueUrl;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.SerializedName;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.HashMap;
import java.util.Map;

@Gson.TypeAdapters
@Value.Immutable
public abstract class CustomResourceResponse {
    @Value.Default
    @SerializedName("StackId")
    public String getStackId() {
        return getCustomResourceRequest().getStackId();
    }

    @Value.Default
    @SerializedName("RequestId")
    public String getRequestId() {
        return getCustomResourceRequest().getRequestId();
    }

    @Value.Default
    @SerializedName("LogicalResourceId")
    public String getLogicalResourceId() {
        return getCustomResourceRequest().getLogicalResourceId();
    }

    @Value.Default
    @SerializedName("PhysicalResourceId")
    public String getPhysicalResourceId() {
        // If this is not specified just use the log stream name
        return getLogStreamNameAsDefault();
    }

    @SerializedName("Status")
    public abstract CustomResourceStatus getStatus();

    @Value.Default
    @SerializedName("Reason")
    public String getReason() {
        // If this is not specified just use the log stream name
        return getLogStreamNameAsDefault();
    }

    @Value.Default
    @SerializedName("NoEcho")
    public boolean getNoEcho() {
        return false;
    }

    @Value.Default
    @SerializedName("Data")
    public Map getData() {
        return new HashMap();
    }

    // Used to get the original request to the sender, must not be serialized with the response
    @JsonIgnore
    public abstract CustomResourceRequest getCustomResourceRequest();

    // Used to get the Lambda context for logging purposes to the sender, must not be serialized with the response
    @JsonIgnore
    public abstract Context getContext();

    private String getLogStreamNameAsDefault() {
        return getContext().getLogStreamName();
    }

    public static class Builder extends ImmutableCustomResourceResponse.Builder {
    }

    public static Builder builder() {
        return new Builder();
    }
}

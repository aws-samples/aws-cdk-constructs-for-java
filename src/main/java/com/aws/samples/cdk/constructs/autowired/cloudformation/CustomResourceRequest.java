package com.aws.samples.cdk.constructs.autowired.cloudformation;

import com.google.gson.annotations.SerializedName;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.Map;
import java.util.Optional;

@Gson.TypeAdapters
@Value.Immutable
public abstract class CustomResourceRequest {
    public static Builder builder() {
        return new Builder();
    }

    @SerializedName("ResourceProperties")
    public abstract Map<String, Object> getResourceProperties();

    @SerializedName("RequestType")
    public abstract String getRequestType();

    @SerializedName("ResponseURL")
    public abstract String getResponseURL();

    @SerializedName("StackId")
    public abstract String getStackId();

    @SerializedName("RequestId")
    public abstract String getRequestId();

    @SerializedName("LogicalResourceId")
    public abstract String getLogicalResourceId();

    @SerializedName("PhysicalResourceId")
    public abstract Optional<String> getPhysicalResourceId();

    @SerializedName("ResourceType")
    public abstract String getResourceType();

    public static class Builder extends ImmutableCustomResourceRequest.Builder {
    }
}

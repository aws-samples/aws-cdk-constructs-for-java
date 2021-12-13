package com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources;

import com.aws.samples.cdk.constructs.iam.permissions.sqs.actions.GetQueueUrl;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class ClientId implements IotResource {
    public abstract String getClientId();

    @Override
    public String getResourceType() {
        return "client";
    }

    @Override
    public String getResourceValue() {
        return getClientId();
    }

    public static class Builder extends ImmutableClientId.Builder {
    }

    public static Builder builder() {
        return new Builder();
    }
}

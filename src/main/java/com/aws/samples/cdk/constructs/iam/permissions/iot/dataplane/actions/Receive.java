package com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.actions;

import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.IotResource;
import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.Topic;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

import static com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions.IOT_RECEIVE_PERMISSION;

@Gson.TypeAdapters
@Value.Immutable
public abstract class Receive implements IotDataPlaneAction {
    public static Builder builder() {
        return new Builder();
    }

    public abstract Topic getTopic();

    @Override
    public String getIamString() {
        return IOT_RECEIVE_PERMISSION;
    }

    @Override
    public IotResource getResource() {
        return getTopic();
    }

    public static class Builder extends ImmutableReceive.Builder {
    }
}

package com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.actions;

import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.Topic;
import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.IotResource;
import com.aws.samples.cdk.constructs.iam.permissions.sqs.actions.GetQueueUrl;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

import static com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions.IOT_RECEIVE_PERMISSION;

@Gson.TypeAdapters
@Value.Immutable
public abstract class Receive implements IotDataPlaneAction {
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

    public static Builder builder() {
        return new Builder();
    }
}

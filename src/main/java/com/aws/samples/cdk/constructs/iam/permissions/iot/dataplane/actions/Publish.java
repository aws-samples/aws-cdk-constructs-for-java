package com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.actions;

import com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions;
import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.IotResource;
import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.Topic;
import com.aws.samples.cdk.constructs.iam.permissions.sqs.actions.GetQueueUrl;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class Publish implements IotDataPlaneAction {
    public abstract Topic getTopic();

    @Override
    public String getIamString() {
        return SharedPermissions.IOT_PUBLISH_PERMISSION;
    }

    @Override
    public IotResource getResource() {
        return getTopic();
    }

    public static class Builder extends ImmutablePublish.Builder {
    }

    public static Builder builder() {
        return new Builder();
    }
}

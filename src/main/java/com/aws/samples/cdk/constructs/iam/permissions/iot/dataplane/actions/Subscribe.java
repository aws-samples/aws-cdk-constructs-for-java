package com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.actions;

import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.IotResource;
import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.TopicFilter;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

import static com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions.IOT_SUBSCRIBE_PERMISSION;

@Gson.TypeAdapters
@Value.Immutable
public abstract class Subscribe implements IotDataPlaneAction {
    public static Builder builder() {
        return new Builder();
    }

    public abstract TopicFilter getTopicFilter();

    @Override
    public String getIamString() {
        return IOT_SUBSCRIBE_PERMISSION;
    }

    @Override
    public IotResource getResource() {
        return getTopicFilter();
    }

    public static class Builder extends ImmutableSubscribe.Builder {
    }
}

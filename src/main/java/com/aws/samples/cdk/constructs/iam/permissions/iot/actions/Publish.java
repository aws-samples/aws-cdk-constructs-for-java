package com.aws.samples.cdk.constructs.iam.permissions.iot.actions;

import com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions;
import com.aws.samples.cdk.constructs.iam.permissions.iot.resources.IotResource;
import com.aws.samples.cdk.constructs.iam.permissions.iot.resources.Topic;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class Publish implements IotAction {
    public abstract Topic getTopic();

    @Override
    public String getIamString() {
        return SharedPermissions.IOT_PUBLISH_PERMISSION;
    }

    @Override
    public IotResource getResource() {
        return getTopic();
    }
}

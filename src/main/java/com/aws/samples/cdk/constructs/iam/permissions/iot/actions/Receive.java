package com.aws.samples.cdk.constructs.iam.permissions.iot.actions;

import com.aws.samples.cdk.constructs.iam.permissions.iot.resources.Topic;
import com.aws.samples.cdk.constructs.iam.permissions.iot.resources.IotResource;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

import static com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions.IOT_RECEIVE_PERMISSION;

@Gson.TypeAdapters
@Value.Immutable
public abstract class Receive implements IotAction {
    public abstract Topic getTopic();

    @Override
    public String getIamString() {
        return IOT_RECEIVE_PERMISSION;
    }

    @Override
    public IotResource getResource() {
        return getTopic();
    }
}

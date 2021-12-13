package com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.actions;

import com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions;
import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.ClientId;
import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.IotResource;
import com.aws.samples.cdk.constructs.iam.permissions.sqs.actions.GetQueueUrl;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class Connect implements IotDataPlaneAction {
    public abstract ClientId getClientId();

    @Override
    public String getIamString() {
        return SharedPermissions.IOT_CONNECT_PERMISSION;
    }

    @Override
    public IotResource getResource() {
        return getClientId();
    }

    public static class Builder extends ImmutableConnect.Builder {
    }

    public static Builder builder() {
        return new Builder();
    }
}

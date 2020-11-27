package com.aws.samples.cdk.constructs.iam.permissions.iot.actions;

import com.aws.samples.cdk.constructs.iam.permissions.IamAction;
import com.aws.samples.cdk.constructs.iam.permissions.IamPermission;
import com.aws.samples.cdk.constructs.iam.permissions.IamResource;
import com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class DescribeEndpoint implements IamPermission {
    @Override
    public Tuple2<IamAction, IamResource> getActionAndResource() {
        IamAction iamAction = new IamAction() {
            @Override
            public String getIamString() {
                return SharedPermissions.IOT_DESCRIBE_ENDPOINT_PERMISSION;
            }

            @Override
            public IamResource getIamResource() {
                return SharedPermissions.allResources;
            }
        };

        return Tuple.of(iamAction, iamAction.getIamResource());
    }
}

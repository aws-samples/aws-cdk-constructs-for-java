package com.aws.samples.cdk.constructs.iam.permissions.sts.actions;

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
public class GetFederationToken implements IamPermission {
    @Override
    public Tuple2<IamAction, IamResource> getActionAndResource() {
        IamAction iamAction = new IamAction() {
            @Override
            public String getIamString() {
                return SharedPermissions.STS_GET_FEDERATION_TOKEN_PERMISSION;
            }

            @Override
            public IamResource getIamResource() {
                return SharedPermissions.IAM_ALL_RESOURCES;
            }
        };

        return Tuple.of(iamAction, iamAction.getIamResource());
    }
}

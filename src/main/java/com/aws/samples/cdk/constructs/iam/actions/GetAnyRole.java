package com.aws.samples.cdk.constructs.iam.actions;

import com.aws.samples.cdk.constructs.iam.permissions.IamAction;
import com.aws.samples.cdk.constructs.iam.permissions.IamPermission;
import com.aws.samples.cdk.constructs.iam.permissions.IamResource;
import com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions;
import com.aws.samples.cdk.constructs.iam.resources.IamAllResources;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public class GetAnyRole implements IamPermission {
    @Override
    public Tuple2<IamAction, IamResource> getActionAndResource() {
        IamAction iamAction = new IamAction() {
            @Override
            public String getIamString() {
                return SharedPermissions.IAM_GET_ROLE_PERMISSION;
            }

            @Override
            public IamResource getIamResource() {
                return new IamAllResources();
            }
        };

        return Tuple.of(iamAction, iamAction.getIamResource());
    }
}

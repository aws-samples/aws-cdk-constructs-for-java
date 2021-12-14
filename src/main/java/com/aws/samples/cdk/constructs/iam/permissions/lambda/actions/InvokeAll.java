package com.aws.samples.cdk.constructs.iam.permissions.lambda.actions;

import com.aws.samples.cdk.constructs.iam.permissions.IamAction;
import com.aws.samples.cdk.constructs.iam.permissions.IamPermission;
import com.aws.samples.cdk.constructs.iam.permissions.IamResource;
import com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions;
import com.aws.samples.cdk.constructs.iam.permissions.lambda.resources.LambdaAllResources;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class InvokeAll implements IamPermission {
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Tuple2<IamAction, IamResource> getActionAndResource() {
        IamAction iamAction = new IamAction() {
            @Override
            public String getIamString() {
                return SharedPermissions.LAMBDA_INVOKE_FUNCTION;
            }

            @Override
            public IamResource getIamResource() {
                return new LambdaAllResources();
            }
        };

        return Tuple.of(iamAction, iamAction.getIamResource());
    }

    public static class Builder extends ImmutableInvokeAll.Builder {
    }
}

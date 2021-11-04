package com.aws.samples.cdk.constructs.iam.permissions.sqs.actions;

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
public abstract class GetQueueUrl implements IamPermission {
    public abstract IamResource getQueue();

    @Override
    public Tuple2<IamAction, IamResource> getActionAndResource() {
        IamAction iamAction = new IamAction() {
            @Override
            public String getIamString() {
                return SharedPermissions.SQS_GET_QUEUE_URL;
            }

            @Override
            public IamResource getIamResource() {
                return getQueue();
            }
        };

        return Tuple.of(iamAction, iamAction.getIamResource());
    }
}

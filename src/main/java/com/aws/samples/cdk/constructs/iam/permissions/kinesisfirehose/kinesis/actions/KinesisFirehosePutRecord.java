package com.aws.samples.cdk.constructs.iam.permissions.kinesisfirehose.kinesis.actions;

import com.aws.samples.cdk.constructs.iam.permissions.IamAction;
import com.aws.samples.cdk.constructs.iam.permissions.IamPermission;
import com.aws.samples.cdk.constructs.iam.permissions.IamResource;
import com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions;
import com.aws.samples.cdk.constructs.iam.permissions.kinesisfirehose.kinesis.resources.KinesisFirehoseAllResources;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class KinesisFirehosePutRecord implements IamPermission {
    @Override
    public Tuple2<IamAction, IamResource> getActionAndResource() {
        IamAction iamAction = new IamAction() {
            @Override
            public String getIamString() {
                return SharedPermissions.KINESIS_PUT_RECORD;
            }

            @Override
            public IamResource getIamResource() {
                return new KinesisFirehoseAllResources();
            }
        };

        return Tuple.of(iamAction, iamAction.getIamResource());
    }
}

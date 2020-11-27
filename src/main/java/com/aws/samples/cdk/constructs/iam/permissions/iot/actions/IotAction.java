package com.aws.samples.cdk.constructs.iam.permissions.iot.actions;

import com.aws.samples.cdk.constructs.iam.permissions.IamAction;
import com.aws.samples.cdk.constructs.iam.permissions.IamPermission;
import com.aws.samples.cdk.constructs.iam.permissions.IamResource;
import com.aws.samples.cdk.constructs.iam.permissions.iot.resources.IotResource;
import io.vavr.Tuple;
import io.vavr.Tuple2;

public interface IotAction extends IamPermission, IamAction {
    IotResource getResource();

    default IamResource getIamResource() {
        return getResource();
    }

    default Tuple2<IamAction, IamResource> getActionAndResource() {
        return Tuple.of(this, getIamResource());
    }
}

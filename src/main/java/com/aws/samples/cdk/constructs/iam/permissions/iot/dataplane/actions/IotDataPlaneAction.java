package com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.actions;

import com.aws.samples.cdk.constructs.iam.permissions.IamAction;
import com.aws.samples.cdk.constructs.iam.permissions.IamPermission;
import com.aws.samples.cdk.constructs.iam.permissions.IamResource;
import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.IotDataPlaneResource;
import io.vavr.Tuple;
import io.vavr.Tuple2;

public interface IotDataPlaneAction extends IamPermission, IamAction {
    IotDataPlaneResource getResource();

    default IamResource getIamResource() {
        return getResource();
    }

    default Tuple2<IamAction, IamResource> getActionAndResource() {
        return Tuple.of(this, getIamResource());
    }
}

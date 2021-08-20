package com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources;

import com.aws.samples.cdk.constructs.iam.permissions.IamResource;

public interface IotDataPlaneResource extends IamResource {
    default String getServiceIdentifier() {
        return "iot";
    }
}

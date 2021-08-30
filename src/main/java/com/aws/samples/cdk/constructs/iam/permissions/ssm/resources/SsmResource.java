package com.aws.samples.cdk.constructs.iam.permissions.ssm.resources;

import com.aws.samples.cdk.constructs.iam.permissions.IamResource;

public interface SsmResource extends IamResource {
    default String getServiceIdentifier() {
        return "ssm";
    }
}

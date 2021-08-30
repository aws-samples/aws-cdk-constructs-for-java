package com.aws.samples.cdk.constructs.iam.permissions.sts.resources;

import com.aws.samples.cdk.constructs.iam.permissions.IamResource;

public interface StsResource extends IamResource {
    default String getServiceIdentifier() {
        return "sts";
    }
}

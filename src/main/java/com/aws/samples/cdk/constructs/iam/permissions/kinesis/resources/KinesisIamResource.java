package com.aws.samples.cdk.constructs.iam.permissions.kinesis.resources;

import com.aws.samples.cdk.constructs.iam.permissions.IamResource;

public interface KinesisIamResource extends IamResource {
    default String getServiceIdentifier() {
        return "kinesis";
    }
}

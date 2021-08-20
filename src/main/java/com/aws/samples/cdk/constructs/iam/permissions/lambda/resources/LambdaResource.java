package com.aws.samples.cdk.constructs.iam.permissions.lambda.resources;

import com.aws.samples.cdk.constructs.iam.permissions.IamResource;

public interface LambdaResource extends IamResource {
    @Override
    default String getResourceValueSeparator() {
        return ":";
    }

    default String getServiceIdentifier() {
        return "lambda";
    }
}

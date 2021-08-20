package com.aws.samples.cdk.constructs.iam.permissions.lambda.resources;

import com.aws.samples.cdk.constructs.iam.permissions.IamResource;

public interface LambdaResource extends IamResource {
    default String getServiceIdentifer() {
        return "lambda";
    }
}

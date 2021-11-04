package com.aws.samples.cdk.constructs.iam.permissions.sqs.resources;

import com.aws.samples.cdk.constructs.iam.permissions.IamResource;

public interface SqsResource extends IamResource {
    @Override
    default String getResourceValueSeparator() {
        return ":";
    }

    @Override
    default String getResourceTypeSeparator() {
        // Since there is only one resource type in SQS the resource type is omitted. We remove the separator to avoid
        //   having double colons show up in the ARN.
        return "";
    }

    default String getServiceIdentifier() {
        return "sqs";
    }
}

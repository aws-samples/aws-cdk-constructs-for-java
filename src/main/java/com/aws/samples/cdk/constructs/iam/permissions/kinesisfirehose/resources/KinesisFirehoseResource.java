package com.aws.samples.cdk.constructs.iam.permissions.kinesisfirehose.resources;

import com.aws.samples.cdk.constructs.iam.permissions.IamResource;

public interface KinesisFirehoseResource extends IamResource {
    default String getServiceIdentifer() {
        return "firehose";
    }
}

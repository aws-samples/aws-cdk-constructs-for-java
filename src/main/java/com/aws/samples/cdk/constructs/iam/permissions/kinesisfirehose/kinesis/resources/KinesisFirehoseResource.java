package com.aws.samples.cdk.constructs.iam.permissions.kinesisfirehose.kinesis.resources;

import com.aws.samples.cdk.constructs.iam.permissions.IamResource;
import com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions;
import io.vavr.collection.List;
import software.amazon.awscdk.core.Fn;

public interface KinesisFirehoseResource extends IamResource {
    default String getServiceIdentifer() {
        return "firehose";
    }
}

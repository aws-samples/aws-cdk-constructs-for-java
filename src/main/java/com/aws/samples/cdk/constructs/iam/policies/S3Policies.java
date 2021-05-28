package com.aws.samples.cdk.constructs.iam.policies;

import com.awslabs.s3.helpers.data.S3Bucket;
import io.vavr.collection.List;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.PolicyStatementProps;

public class S3Policies {
    public static final String S3_PERMISSION_PREFIX = "s3";
    public static final String S3_PUT_OBJECT_PERMISSION = String.join(":", S3_PERMISSION_PREFIX, "PutObject");

    public PolicyStatement getPutObjectPolicyStatementForBucket(S3Bucket s3Bucket) {
        String bucketWildcardResource = String.join("", "arn:aws:s3:::", s3Bucket.bucket(), "/*");

        PolicyStatementProps s3PolicyStatementProps = PolicyStatementProps.builder()
                .effect(Effect.ALLOW)
                .resources(List.of(bucketWildcardResource).asJava())
                .actions(List.of(S3_PUT_OBJECT_PERMISSION).asJava())
                .build();
        return new PolicyStatement(s3PolicyStatementProps);
    }
}

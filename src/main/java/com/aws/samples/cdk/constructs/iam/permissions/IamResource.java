package com.aws.samples.cdk.constructs.iam.permissions;

import software.amazon.awscdk.core.Aws;
import software.amazon.awssdk.core.SdkSystemSetting;

public interface IamResource {
    String getIamString();

    default String getAccountId() {
        if (SharedPermissions.isRunningInLambda()) {
            // Running inside of Lambda, use actual values
            return SharedPermissions.getAccountId();
        }

        // Running inside of CDK, use CloudFormation placeholders
        return Aws.ACCOUNT_ID;
    }

    default String getRegion() {
        if (SharedPermissions.isRunningInLambda()) {
            // Running inside of Lambda, use actual values
            return SdkSystemSetting.AWS_REGION.getStringValueOrThrow();
        }

        // Running inside of CDK, use CloudFormation placeholders
        return Aws.REGION;
    }
}

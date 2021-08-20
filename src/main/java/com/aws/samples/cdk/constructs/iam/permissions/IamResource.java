package com.aws.samples.cdk.constructs.iam.permissions;

import io.vavr.collection.List;
import software.amazon.awscdk.core.Aws;
import software.amazon.awscdk.core.Fn;
import software.amazon.awssdk.core.SdkSystemSetting;

public interface IamResource {
    default String getIamString() {
        String delimiter = "";
        List<String> elements = List.of("arn:aws:", getServiceIdentifier(), ":", getRegion(), ":", getAccountId(), ":", getResourceType(), "/", getResourceValue());

        if (SharedPermissions.isRunningInLambda()) {
            // Running inside of Lambda, use a normal string
            return String.join(delimiter, elements);
        } else {
            // Running inside of CDK, use CloudFormation join function
            return Fn.join(delimiter, elements.asJava());
        }
    }

    default String getServiceIdentifier() {
        return null;
    }

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

    /**
     * This value is used in ARNs (e.g. "arn:aws:SERVICE_IDENTIFIER:REGION:ACCOUNT_ID:RESOURCE_TYPE/RESOURCE_VALUE")
     *
     * @return
     */
    String getResourceValue();

    /**
     * This value is used in ARNs (e.g. "arn:aws:SERVICE_IDENTIFIER:REGION:ACCOUNT_ID:RESOURCE_TYPE/RESOURCE_VALUE")
     *
     * @return
     */
    default String getResourceType() {
        return null;
    }
}

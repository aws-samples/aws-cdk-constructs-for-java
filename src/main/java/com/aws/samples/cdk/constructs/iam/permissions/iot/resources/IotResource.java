package com.aws.samples.cdk.constructs.iam.permissions.iot.resources;

import com.aws.samples.cdk.constructs.iam.permissions.IamResource;
import software.amazon.awscdk.core.Fn;

import java.util.Arrays;

public interface IotResource extends IamResource {
    default String getIamString() {
        return Fn.join("", Arrays.asList("arn:aws:iot:", getRegion(), ":", getAccountId(), ":", getResourceType(), "/", getResourceValue()));
    }

    /**
     * This value is used in ARNs (e.g. "arn:aws:iot:REGION:ACCOUNT_ID:RESOURCE_TYPE/RESOURCE_VALUE")
     *
     * @return
     */
    String getResourceValue();

    /**
     * This value is used in ARNs (e.g. "arn:aws:iot:REGION:ACCOUNT_ID:RESOURCE_TYPE/RESOURCE_VALUE")
     *
     * @return
     */
    String getResourceType();
}

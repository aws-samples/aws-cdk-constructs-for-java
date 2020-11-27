package com.aws.samples.cdk.constructs.iam.permissions.iot.resources;

import com.aws.samples.cdk.constructs.iam.permissions.IamResource;
import software.amazon.awscdk.core.Aws;
import software.amazon.awscdk.core.Fn;

import java.util.Arrays;

public interface IotResource extends IamResource {
    default String getIamString() {
        return Fn.join("", Arrays.asList("arn:aws:iot:", Aws.REGION, ":", Aws.ACCOUNT_ID, ":", getResourceType(), "/", getResourceValue()));
    }

    String getResourceValue();

    String getResourceType();
}

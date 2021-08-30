package com.aws.samples.cdk.constructs.iam.resources;

import com.aws.samples.cdk.constructs.iam.permissions.IamResource;
import com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions;

public interface AllResources extends IamResource {
    @Override
    default String getIamString() {
        return SharedPermissions.ALL_RESOURCES;
    }

    @Override
    default String getResourceValue() {
        throw new RuntimeException("This should never be called");
    }
}

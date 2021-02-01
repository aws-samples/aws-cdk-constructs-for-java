package com.aws.samples.cdk.constructs.iam.permissions;

import io.vavr.collection.List;

public interface HasIamPermissions {
    List<IamPermission> getPermissions();
}

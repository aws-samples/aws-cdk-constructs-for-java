package com.aws.samples.cdk.constructs.iam.permissions;

import java.util.List;

public interface HasIamPermissions {
    List<IamPermission> getPermissions();
}
package com.aws.samples.cdk.constructs.iam.resources;

import com.aws.samples.cdk.constructs.iam.permissions.IamResource;
import com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions;

public class IamAllResources implements AllResources {
    @Override
    public String getServiceIdentifier() {
        return "iam";
    }
}

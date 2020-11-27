package com.aws.samples.cdk.constructs.iam.permissions;

public interface IamAction {
    String getIamString();

    IamResource getIamResource();
}

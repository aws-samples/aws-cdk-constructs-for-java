package com.aws.samples.cdk.constructs.iam.resources;

public class IamAllResources implements AllResources {
    @Override
    public String getServiceIdentifier() {
        return "iam";
    }
}

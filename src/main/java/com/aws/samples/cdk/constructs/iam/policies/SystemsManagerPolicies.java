package com.aws.samples.cdk.constructs.iam.policies;

import software.amazon.awscdk.services.iam.ServicePrincipal;

public class SystemsManagerPolicies {
    public static final ServicePrincipal SYSTEMS_MANAGER_SERVICE_PRINCIPAL = new ServicePrincipal("ssm");
}

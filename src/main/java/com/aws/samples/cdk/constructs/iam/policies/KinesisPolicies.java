package com.aws.samples.cdk.constructs.iam.policies;

import software.amazon.awscdk.services.iam.ServicePrincipal;

public class KinesisPolicies {
    public static final ServicePrincipal KINESIS_SERVICE_PRINCIPAL = new ServicePrincipal("kinesis");
}

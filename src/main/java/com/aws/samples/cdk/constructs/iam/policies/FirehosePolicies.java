package com.aws.samples.cdk.constructs.iam.policies;

import software.amazon.awscdk.services.iam.ServicePrincipal;

public class FirehosePolicies {
    public static final ServicePrincipal FIREHOSE_SERVICE_PRINCIPAL = new ServicePrincipal("firehose");
}

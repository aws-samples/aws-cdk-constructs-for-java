package com.aws.samples.cdk.constructs.iam.policies;

import software.amazon.awscdk.services.iam.ServicePrincipal;

public class EcsTaskPolicies {
    public static final ServicePrincipal ECS_TASK_PRINCIPAL = new ServicePrincipal("ecs-tasks");
}

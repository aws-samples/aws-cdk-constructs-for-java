package com.aws.samples.cdk.constructs.iam.policies;

import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.PolicyStatementProps;
import software.amazon.awscdk.services.iam.ServicePrincipal;

import java.util.Collections;

import static com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions.ALL_RESOURCES;
import static com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions.IOT_SEARCH_INDEX_PERMISSION;

public class IotPolicies {
    public static final ServicePrincipal IOT_SERVICE_PRINCIPAL = new ServicePrincipal("iot");
    private static final PolicyStatementProps sqsPolicyStatementProps = PolicyStatementProps.builder()
            .effect(Effect.ALLOW)
            .resources(Collections.singletonList(ALL_RESOURCES))
            .actions(Collections.singletonList(IOT_SEARCH_INDEX_PERMISSION))
            .build();

    public static final PolicyStatement searchIndexPolicyStatement = new PolicyStatement(sqsPolicyStatementProps);
}

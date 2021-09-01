package com.aws.samples.cdk.constructs.iam.policies;

import software.amazon.awscdk.services.iam.*;

import java.util.Collections;

import static com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions.ALL_RESOURCES;
import static com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions.IOT_SEARCH_INDEX_PERMISSION;

public class IotPolicies {
    public static final ServicePrincipal IOT_SERVICE_PRINCIPAL = new ServicePrincipal("iot");

    private static final String AWS_IOT_DATA_ACCESS_POLICY_NAME = "AWSIoTDataAccess";
    public static final IManagedPolicy AWS_IOT_DATA_ACCESS_POLICY = ManagedPolicy.fromAwsManagedPolicyName(AWS_IOT_DATA_ACCESS_POLICY_NAME);

    private static final String AWS_IOT_CONFIG_READ_ONLY_ACCESS_POLICY_NAME = "AWSIoTConfigReadOnlyAccess";
    public static final IManagedPolicy AWS_IOT_CONFIG_READ_ONLY_ACCESS_POLICY = ManagedPolicy.fromAwsManagedPolicyName(AWS_IOT_CONFIG_READ_ONLY_ACCESS_POLICY_NAME);

    private static final PolicyStatementProps SEARCH_INDEX_STATEMENT_PROPS = PolicyStatementProps.builder()
            .effect(Effect.ALLOW)
            .resources(Collections.singletonList(ALL_RESOURCES))
            .actions(Collections.singletonList(IOT_SEARCH_INDEX_PERMISSION))
            .build();

    public static final PolicyStatement searchIndexPolicyStatement = new PolicyStatement(SEARCH_INDEX_STATEMENT_PROPS);
}

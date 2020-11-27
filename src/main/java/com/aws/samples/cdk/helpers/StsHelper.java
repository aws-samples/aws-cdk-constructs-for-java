package com.aws.samples.cdk.helpers;

import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.PolicyStatementProps;
import software.amazon.awscdk.services.iam.Role;

import java.util.Optional;

import static com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions.ALL_RESOURCES;
import static com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions.STS_ASSUME_ROLE;
import static java.util.Collections.singletonList;

public class StsHelper {
    public static PolicyStatement assumeRolePolicyStatement = getAssumeRolePolicyStatement(Optional.empty());

    @NotNull
    public static PolicyStatement getAssumeRolePolicyStatement(Optional<Role> optionalRole) {
        PolicyStatementProps iotPolicyStatementProps = PolicyStatementProps.builder()
                .effect(Effect.ALLOW)
                .resources(singletonList(optionalRole.map(Role::getRoleArn).orElse(ALL_RESOURCES)))
                .actions(singletonList(STS_ASSUME_ROLE))
                .build();

        return new PolicyStatement(iotPolicyStatementProps);
    }
}
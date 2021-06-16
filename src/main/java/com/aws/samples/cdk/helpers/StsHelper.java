package com.aws.samples.cdk.helpers;

import io.vavr.collection.List;
import io.vavr.control.Option;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.PolicyStatementProps;
import software.amazon.awscdk.services.iam.Role;

import static com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions.ALL_RESOURCES;
import static com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions.STS_ASSUME_ROLE;

public class StsHelper {
    public static PolicyStatement assumeRolePolicyStatement = getAssumeRolePolicyStatement(Option.none());

    @NotNull
    public static PolicyStatement getAssumeRolePolicyStatement(Option<Role> roleOption) {
        PolicyStatementProps policyStatementProps = PolicyStatementProps.builder()
                .effect(Effect.ALLOW)
                .resources(List.of(roleOption.map(Role::getRoleArn).getOrElse(ALL_RESOURCES)).asJava())
                .actions(List.of(STS_ASSUME_ROLE).asJava())
                .build();

        return new PolicyStatement(policyStatementProps);
    }
}

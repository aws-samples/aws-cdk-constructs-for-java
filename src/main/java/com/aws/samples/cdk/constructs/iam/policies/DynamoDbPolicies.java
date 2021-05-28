package com.aws.samples.cdk.constructs.iam.policies;

import io.vavr.collection.List;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.PolicyStatementProps;

public class DynamoDbPolicies {
    public static PolicyStatement getPolicyStatementForTable(Table table, String action) {
        PolicyStatementProps dynamoDbPolicyStatementProps = PolicyStatementProps.builder()
                .effect(Effect.ALLOW)
                .resources(List.of(table.getTableArn()).asJava())
                .actions(List.of(action).asJava())
                .build();
        return new PolicyStatement(dynamoDbPolicyStatementProps);
    }
}

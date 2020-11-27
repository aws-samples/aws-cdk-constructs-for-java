package com.aws.samples.cdk.constructs.iam.policies;

import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.PolicyStatementProps;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions.*;

public class CloudWatchLogsPolicies {
    private static final List<String> MINIMAL_LOGGING_ACTIONS = Arrays.asList(
            CLOUDWATCH_LOGS_CREATE_LOG_GROUP,
            CLOUDWATCH_LOGS_CREATE_LOG_STREAM,
            CLOUDWATCH_LOGS_PUT_LOG_EVENTS,
            CLOUDWATCH_LOGS_DESCRIBE_LOG_STREAMS);
    private static final List<String> ALL_LOG_GROUPS_AND_LOG_STREAMS = Collections.singletonList("arn:aws:logs:*:*:*");

    private static final PolicyStatementProps cloudWatchPolicyStatementProps = PolicyStatementProps.builder()
            .effect(Effect.ALLOW)
            .resources(ALL_LOG_GROUPS_AND_LOG_STREAMS)
            .actions(MINIMAL_LOGGING_ACTIONS)
            .build();

    public static final PolicyStatement minimalCloudWatchEventsLoggingPolicy = new PolicyStatement(cloudWatchPolicyStatementProps);
}

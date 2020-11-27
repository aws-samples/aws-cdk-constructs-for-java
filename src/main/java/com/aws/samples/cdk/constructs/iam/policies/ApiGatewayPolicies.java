package com.aws.samples.cdk.constructs.iam.policies;

import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.iam.IManagedPolicy;
import software.amazon.awscdk.services.iam.ManagedPolicy;

public class ApiGatewayPolicies {
    public static @NotNull IManagedPolicy getPushToCloudWatchLogsManagedPolicy(Stack stack, String name) {
        return ManagedPolicy.fromManagedPolicyArn(stack, String.join("", "PushToCloudWatchLogs", name), "arn:aws:iam::aws:policy/service-role/AmazonAPIGatewayPushToCloudWatchLogs");
    }
}

package com.aws.samples.cdk.helpers;

import com.aws.samples.cdk.constructs.iam.policies.ApiGatewayPolicies;
import com.aws.samples.cdk.constructs.iam.policies.LambdaPolicies;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.iam.*;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LambdaHelper {
    public static Function buildIotEventLambda(Stack stack, String functionNamePrefix, Role role, Runtime runtime, Map<String, String> defaultEnvironment, Map<String, String> additionalEnvironment, String assetName, String handler, Duration lambdaFunctionTimeout) {
        return buildIotEventLambda(stack, functionNamePrefix, role, runtime, defaultEnvironment, additionalEnvironment, Code.fromAsset(assetName), handler, lambdaFunctionTimeout);
    }

    public static Function buildIotEventLambda(Stack stack, String functionNamePrefix, Role role, Runtime runtime, Map<String, String> defaultEnvironment, Map<String, String> additionalEnvironment, AssetCode assetCode, String handler, Duration lambdaFunctionTimeout) {
        Map<String, String> environment = new HashMap<>(defaultEnvironment);
        environment.putAll(additionalEnvironment);

        FunctionProps functionProps = FunctionProps.builder()
                .code(assetCode)
                .handler(handler)
                .memorySize(1024)
                .timeout(lambdaFunctionTimeout)
                .environment(environment)
                .runtime(runtime)
                .role(role)
                .build();

        return new Function(stack, functionNamePrefix + "Lambda", functionProps);
    }

    public static Role getRoleAssumedByLambda(Stack stack, String name, Optional<PolicyDocumentProps> optionalPolicyDocumentProps) {
        // This policy refers to API Gateway but is applicable for Lambda functions that need to write logs to CloudWatch Logs
        //   whether they're invoked by API Gateway or not
        @NotNull IManagedPolicy apiGatewayPushToCloudWatchLogsManagedPolicy = ApiGatewayPolicies.getPushToCloudWatchLogsManagedPolicy(stack, name);

        Map<String, PolicyDocument> policyDocuments = new HashMap<>();
        optionalPolicyDocumentProps.ifPresent(policyDocumentProps -> policyDocuments.put("root", new PolicyDocument(policyDocumentProps)));

        RoleProps roleProps = RoleProps.builder()
                .assumedBy(LambdaPolicies.LAMBDA_SERVICE_PRINCIPAL)
                .inlinePolicies(policyDocuments)
                .managedPolicies(Collections.singletonList(apiGatewayPushToCloudWatchLogsManagedPolicy))
                .build();

        return new Role(stack, name + "Role", roleProps);
    }
}

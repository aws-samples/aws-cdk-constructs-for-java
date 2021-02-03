package com.aws.samples.cdk.helpers;

import com.aws.samples.cdk.constructs.iam.policies.ApiGatewayPolicies;
import com.aws.samples.cdk.constructs.iam.policies.LambdaPolicies;
import io.vavr.Tuple;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.iam.PolicyDocument;
import software.amazon.awscdk.services.iam.PolicyDocumentProps;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.RoleProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.*;

import java.util.Collections;

public class LambdaHelper {
    public static Function buildIotEventLambda(Stack stack, String functionNamePrefix, Role role, Runtime runtime, Map<String, String> defaultEnvironment, Map<String, String> additionalEnvironment, String assetName, String handler, Duration lambdaFunctionTimeout) {
        return buildIotEventLambda(stack, functionNamePrefix, role, runtime, defaultEnvironment, additionalEnvironment, Code.fromAsset(assetName), handler, lambdaFunctionTimeout);
    }

    public static Function buildIotEventLambda(Stack stack, String functionNamePrefix, Role role, Runtime runtime, Map<String, String> defaultEnvironment, Map<String, String> additionalEnvironment, AssetCode assetCode, String handler, Duration lambdaFunctionTimeout) {
        Map<String, String> environment = defaultEnvironment.merge(additionalEnvironment);

        FunctionProps functionProps = FunctionProps.builder()
                .code(assetCode)
                .handler(handler)
                .memorySize(1024)
                .timeout(lambdaFunctionTimeout)
                .environment(environment.toJavaMap())
                .runtime(runtime)
                .role(role)
                .tracing(Tracing.ACTIVE)
                .build();

        return new Function(stack, functionNamePrefix + "Lambda", functionProps);
    }

    public static Role getRoleAssumedByLambda(Stack stack, String name, Option<PolicyDocumentProps> policyDocumentPropsOption) {
        Map<String, PolicyDocument> policyDocuments = policyDocumentPropsOption
                .map(policyDocumentProps -> Tuple.of("root", new PolicyDocument(policyDocumentProps)))
                .toMap(tuple2 -> tuple2._1, tuple2 -> tuple2._2);

        RoleProps roleProps = RoleProps.builder()
                .assumedBy(LambdaPolicies.LAMBDA_SERVICE_PRINCIPAL)
                .inlinePolicies(policyDocuments.toJavaMap())
                // This policy refers to API Gateway but is applicable for Lambda functions that need to write logs to CloudWatch Logs
                //   whether they're invoked by API Gateway or not
                .managedPolicies(Collections.singletonList(ApiGatewayPolicies.getPushToCloudWatchLogsManagedPolicy(stack, name)))
                .build();

        return new Role(stack, name + "Role", roleProps);
    }
}

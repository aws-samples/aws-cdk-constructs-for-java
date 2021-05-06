package com.aws.samples.cdk.helpers;

import com.aws.samples.cdk.constructs.autowired.lambda.LambdaFunction;
import com.aws.samples.cdk.constructs.iam.policies.ApiGatewayPolicies;
import com.aws.samples.cdk.constructs.iam.policies.LambdaPolicies;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.iam.PolicyDocument;
import software.amazon.awscdk.services.iam.PolicyDocumentProps;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.RoleProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.*;

import java.io.File;
import java.util.Collections;

import static com.aws.samples.cdk.helpers.ReflectionHelper.*;

/**
 * Functions that specifically help with creating Lambda functions
 */
public class LambdaHelper {
    public static final Duration DEFAULT_LAMBDA_FUNCTION_TIMEOUT = Duration.seconds(10);
    public static final int DEFAULT_LAMBDA_FUNCTION_MEMORY_IN_MB = 1024;
    public static final Runtime DEFAULT_LAMBDA_FUNCTION_RUNTIME = Runtime.JAVA_11;
    public static final String DUMMY = "dummy";
    private static final Logger log = LoggerFactory.getLogger(LambdaHelper.class);

    private static List<Class<LambdaFunction>> findLambdaFunctionsInJar(File file) {
        return findClassesInJarImplementingInterface(file, LambdaFunction.class);
    }

    public static List<Function> getLambdaFunctions(Stack stack, File file, Option<FunctionProps.Builder> functionPropsBuilderOption) {
        return findLambdaFunctionsInJar(file)
                .map(clazz -> getFunction(stack, file, clazz.getName(), functionPropsBuilderOption));
    }

    public static Function getFunction(Stack stack, File file, String className, Option<FunctionProps.Builder> functionPropsBuilderOption) {
        String lastClassName = getLastClassName(className);

        Option<PolicyDocumentProps> policyDocumentPropsOption = ReflectionHelper.getPolicyDocumentForClassOption(file, className);

        if (policyDocumentPropsOption.isEmpty()) {
            log.warn("No permissions found for " + lastClassName);
        }

        Role role = getRoleAssumedByLambda(stack, lastClassName, policyDocumentPropsOption);

        String handlerName = String.join("::", className, HANDLE_REQUEST);

        return buildLambda(stack, lastClassName, role, HashMap.empty(), file.getAbsolutePath(), handlerName, functionPropsBuilderOption);
    }

    public static Function buildLambda(Stack stack, String functionNamePrefix, Role role, Map<String, String> defaultEnvironment, String assetName, String handler, Option<FunctionProps.Builder> functionPropsBuilderOption) {
        return buildLambda(stack, functionNamePrefix, role, defaultEnvironment, Code.fromAsset(assetName), handler, functionPropsBuilderOption);
    }

    public static Function buildLambda(Stack stack, String functionNamePrefix, Role role, Map<String, String> defaultEnvironment, AssetCode assetCode, String handler, Option<FunctionProps.Builder> functionPropsBuilderOption) {
        FunctionProps.Builder functionPropsBuilder = FunctionProps.builder()
                // Start out with the defaults
                .environment(defaultEnvironment.toJavaMap())
                .timeout(DEFAULT_LAMBDA_FUNCTION_TIMEOUT)
                .runtime(DEFAULT_LAMBDA_FUNCTION_RUNTIME)
                .memorySize(DEFAULT_LAMBDA_FUNCTION_MEMORY_IN_MB);

        if (functionPropsBuilderOption.isDefined()) {
            FunctionProps.Builder tempFunctionPropsBuilder = functionPropsBuilderOption.get()
                    // Add a dummy code value to prevent the builder from failing
                    .code(Code.fromInline(DUMMY))
                    // Add a dummy handler value to prevent the builder from failing
                    .handler(DUMMY);
            FunctionProps existingFunctionProps = tempFunctionPropsBuilder.build();

            // Sanity checks (the build() method did not check for these when this code was written)
            Option.of(existingFunctionProps.getTimeout()).getOrElseThrow(() -> new RuntimeException("Existing Lambda function props did not specify a timeout"));
            Option.of(existingFunctionProps.getMemorySize()).getOrElseThrow(() -> new RuntimeException("Existing Lambda function props did not specify a memory size"));

            // Get the existing environment
            HashMap<String, String> existingEnvironment = Option.of(existingFunctionProps.getEnvironment())
                    // Convert it to a vavr HashMap
                    .map(HashMap::ofAll)
                    // Use an empty map if no environment was specified
                    .getOrElse(HashMap::empty);

            Map<String, String> environment = defaultEnvironment.merge(existingEnvironment);

            // Replace the empty builder with our existing builder and populate the environment
            functionPropsBuilder = functionPropsBuilderOption.get()
                    .environment(environment.toJavaMap());
        }

        // Populate the rest of the values that all functions need
        FunctionProps functionProps = functionPropsBuilder
                .code(assetCode)
                .handler(handler)
                .role(role)
                .tracing(Tracing.ACTIVE)
                .build();

        return new Function(stack, String.join("-", functionNamePrefix, "lambda"), functionProps);
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

        return new Role(stack, String.join("-", name , "role"), roleProps);
    }
}

package com.aws.samples.cdk.helpers;

import com.aws.samples.cdk.helpers.data.AwsLambdaServlet;
import com.aws.samples.lambda.servlet.automation.GeneratedClassFinder;
import com.aws.samples.lambda.servlet.automation.GeneratedClassInfo;
import io.vavr.Tuple3;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.apigateway.*;
import software.amazon.awscdk.services.iam.PolicyDocumentProps;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;

import java.io.File;
import java.util.jar.JarFile;

import static com.aws.samples.cdk.helpers.IotHelper.DEFAULT_LAMBDA_FUNCTION_TIMEOUT;
import static com.aws.samples.cdk.helpers.LambdaHelper.getRoleAssumedByLambda;
import static com.aws.samples.cdk.helpers.ReflectionHelper.HANDLE_REQUEST;
import static java.util.Collections.singletonList;

public class ServerlessHelper {
    private static final Logger log = LoggerFactory.getLogger(ServerlessHelper.class);
    public static final String ROOT_CATCHALL = "/*";
    public static final String AUTHORIZERS = "AUTHORIZERS";

    private static List<GeneratedClassInfo> getGeneratedClassInfo(File file) {
        JarFile jarFile = Try.of(() -> new JarFile(file)).get();

        GeneratedClassFinder generatedClassFinder = new GeneratedClassFinder();
        return generatedClassFinder.getGeneratedClassList(jarFile);
    }

    private static Function getFunction(Stack stack, File file, String className, Option<Map<String, String>> environmentOption) {
        String[] splitClassName = className.split("\\.");
        String lastClassName = splitClassName[splitClassName.length - 1];

        Map<String, String> additionalEnvironment = environmentOption.getOrElse(HashMap.empty());

        Option<PolicyDocumentProps> policyDocumentPropsOption = ReflectionHelper.getOptionPolicyDocumentForClass(file, className);

        if (policyDocumentPropsOption.isEmpty()) {
            log.warn("No permissions found for " + lastClassName);
        }

        Role role = getRoleAssumedByLambda(stack, lastClassName, policyDocumentPropsOption);

        String handlerName = String.join("::", className, HANDLE_REQUEST);

        return LambdaHelper.buildIotEventLambda(stack, lastClassName, role, Runtime.JAVA_11, HashMap.empty(), additionalEnvironment, file.getAbsolutePath(), handlerName, DEFAULT_LAMBDA_FUNCTION_TIMEOUT);
    }

    public static List<AwsLambdaServlet> getAwsLambdaServlets(Stack stack, File file) {
        return getAwsLambdaServlets(stack, file, Option.none());
    }

    public static List<AwsLambdaServlet> getAwsLambdaServlets(Stack stack, File file, Option<Map<String, String>> OptionEnvironment) {
        return getGeneratedClassInfo(file)
                .map(generatedClassInfo -> new AwsLambdaServlet(generatedClassInfo, getFunction(stack, file, generatedClassInfo.className, OptionEnvironment)));
    }

    public static LambdaRestApi buildLambdaRestApiIfPossible(Stack stack, List<AwsLambdaServlet> awsLambdaServlets) {
        // Special case, this gets the root API
        Option<LambdaRestApi> lambdaRestApiOption = awsLambdaServlets
                .find(value -> value.generatedClassInfo.path.equals(ROOT_CATCHALL))
                .map(value -> value.function)
                .map(function -> buildLambdaRestApiForRootFunction(stack, function));

        LambdaRestApi lambdaRestApi = lambdaRestApiOption.getOrElseThrow(() -> new RuntimeException("Lambda REST API was not built because the root handler could not be found"));

        addNonRootFunctions(lambdaRestApi, awsLambdaServlets);

        return lambdaRestApi;
    }

    private static LambdaRestApi buildLambdaRestApiForRootFunction(Stack stack, Function function) {
        String apiName = stack.getStackName().replace("-stack", "");

        return LambdaRestApi.Builder.create(stack, apiName)
                .restApiName(apiName)
                .description("CDK built serverless API for [" + stack.getStackName() + "]")
                .endpointConfiguration(EndpointConfiguration.builder().types(singletonList(EndpointType.REGIONAL)).build())
                .binaryMediaTypes(singletonList("*/*"))
                .handler(function)
                .build();
    }

    private static List<IResource> addNonRootFunctions(LambdaRestApi lambdaRestApi, List<AwsLambdaServlet> classInfoAndFunctionList) {
        return classInfoAndFunctionList
                .filter(value -> !value.generatedClassInfo.path.equals(ROOT_CATCHALL))
                .map(ServerlessHelper::trimLeadingSlashIfNecessary)
                .map(value -> new Tuple3<>(value.generatedClassInfo.path, value.function, lambdaRestApi))
                .map(ServerlessHelper::buildResource);
    }

    private static IResource buildResource(Tuple3<String, Function, LambdaRestApi> input) {
        String path = input._1;
        Function function = input._2;
        LambdaRestApi lambdaRestApi = input._3;
        List<String> cumulativePath = List.empty();
        Stack stack = lambdaRestApi.getStack();

        @NotNull IResource parent = lambdaRestApi.getRoot();

        for (String currentPath : path.split("/")) {
            if (currentPath.equals("*")) {
                // TODO: Implement wildcards other than the root
                continue;
            }

            cumulativePath = cumulativePath.append(currentPath);
            String cumulativePathString = String.join("-", cumulativePath);

            IResource resource = parent.getResource(currentPath);

            if (resource == null) {
                // Only create this if it doesn't exist already
                resource = Resource.Builder.create(stack, cumulativePathString)
                        .parent(parent)
                        .pathPart(currentPath)
                        .build();
            }

            parent = resource;
        }

        parent.addMethod("ANY", getLambdaIntegration(function));

        return parent;
    }

    private static AwsLambdaServlet trimLeadingSlashIfNecessary(AwsLambdaServlet awsLambdaServlet) {
        if (awsLambdaServlet.generatedClassInfo.path.startsWith("/")) {
            // Trim leading slash so it doesn't mess up our split and give us an empty value first
            awsLambdaServlet.generatedClassInfo.path = awsLambdaServlet.generatedClassInfo.path.substring(1);
        }

        return awsLambdaServlet;
    }

    private static LambdaIntegration getLambdaIntegration(Function function) {
        return LambdaIntegration.Builder
                .create(function)
                .build();
    }
}

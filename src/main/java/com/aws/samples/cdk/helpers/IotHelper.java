package com.aws.samples.cdk.helpers;

import com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions;
import com.aws.samples.cdk.constructs.iot.authorizer.IotCustomAuthorizer;
import io.vavr.control.Try;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.Fn;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.PolicyStatementProps;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iot.CfnAuthorizer;
import software.amazon.awscdk.services.iot.CfnAuthorizerProps;
import software.amazon.awscdk.services.iot.CfnTopicRule;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.*;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions.*;
import static com.aws.samples.cdk.helpers.CdkHelper.NO_SEPARATOR;
import static com.aws.samples.cdk.helpers.LambdaHelper.getRoleAssumedByLambda;
import static com.aws.samples.cdk.helpers.ReflectionHelper.HANDLE_REQUEST;
import static com.aws.samples.cdk.helpers.ReflectionHelper.findClassesInJarImplementingInterface;
import static java.util.Collections.singletonList;

public class IotHelper {
    public static final Duration DEFAULT_LAMBDA_FUNCTION_TIMEOUT = Duration.seconds(10);
    public static final String TOPIC = ":topic/";
    public static final String ALL_SUFFIX = "/*";
    public static final String TOPICFILTER = ":topicfilter/";
    public static final String IOT_LAMBDA_PERMISSIONS = "IotLambdaPermissions";

    public static final PolicyStatement createThingPolicyStatement = getAllowAllPolicyStatement(IOT_CREATE_THING_PERMISSION);
    public static final PolicyStatement createThingGroupPolicyStatement = getAllowAllPolicyStatement(IOT_CREATE_THING_GROUP_PERMISSION);
    public static final PolicyStatement updateThingGroupsForThingPolicyStatement = getAllowAllPolicyStatement(IOT_UPDATE_THING_GROUPS_FOR_THING_PERMISSION);
    public static final PolicyStatement updateThingShadowPolicyStatement = getAllowAllPolicyStatement(IOT_UPDATE_THING_SHADOW_PERMISSION);

    public static CfnPermission allowIotTopicRuleToInvokeLambdaFunction(Stack stack, CfnTopicRule topicRule, Function function, String permissionNamePrefix) {
        String iotServicePrincipal = Fn.join(".", Arrays.asList("iot", stack.getUrlSuffix()));
        CfnPermissionProps cfnPermissionProps = CfnPermissionProps.builder()
                .sourceArn(topicRule.getAttrArn())
                .action(SharedPermissions.LAMBDA_INVOKE_FUNCTION)
                .principal(iotServicePrincipal)
                .sourceAccount(stack.getAccount())
                .functionName(function.getFunctionArn())
                .build();

        return new CfnPermission(stack, permissionNamePrefix + IOT_LAMBDA_PERMISSIONS, cfnPermissionProps);
    }

    public static CfnPermission allowIotAuthorizerToInvokeLambdaFunction(Stack stack, CfnAuthorizer cfnAuthorizer, Function function, String permissionNamePrefix) {
        String iotServicePrincipal = Fn.join(".", Arrays.asList("iot", stack.getUrlSuffix()));

        CfnPermissionProps cfnPermissionProps = CfnPermissionProps.builder()
                .sourceArn(cfnAuthorizer.getAttrArn())
                .action(SharedPermissions.LAMBDA_INVOKE_FUNCTION)
                .principal(iotServicePrincipal)
                .functionName(function.getFunctionArn())
                .build();

        return new CfnPermission(stack, permissionNamePrefix + IOT_LAMBDA_PERMISSIONS, cfnPermissionProps);
    }

    @NotNull
    public static PolicyStatement getPublishToTopicPolicyStatement(Stack stack, String topic) {
        PolicyStatementProps iotPolicyStatementProps = PolicyStatementProps.builder()
                .effect(Effect.ALLOW)
                .resources(singletonList(Fn.join("", Arrays.asList("arn:aws:iot:", stack.getRegion(), ":", stack.getAccount(), TOPIC, topic))))
                .actions(singletonList(IOT_PUBLISH_PERMISSION))
                .build();

        return new PolicyStatement(iotPolicyStatementProps);
    }

    private static final PolicyStatementProps describeEndpointPolicyStatementProps = PolicyStatementProps.builder()
            .effect(Effect.ALLOW)
            .resources(singletonList(ALL_RESOURCES))
            .actions(singletonList(IOT_DESCRIBE_ENDPOINT_PERMISSION))
            .build();

    public static final PolicyStatement describeEndpointPolicyStatement = new PolicyStatement(describeEndpointPolicyStatementProps);

    @NotNull
    public static PolicyStatement getPublishToTopicPrefixPolicyStatement(Stack stack, String topicPrefix) {
        PolicyStatementProps iotPolicyStatementProps = PolicyStatementProps.builder()
                .effect(Effect.ALLOW)
                .resources(singletonList(Fn.join(NO_SEPARATOR, Arrays.asList("arn:aws:iot:", stack.getRegion(), ":", stack.getAccount(), TOPIC, topicPrefix, ALL_SUFFIX))))
                .actions(singletonList(SharedPermissions.IOT_PUBLISH_PERMISSION))
                .build();
        return new PolicyStatement(iotPolicyStatementProps);
    }

    @NotNull
    public static PolicyStatement getSubscribeToTopicPrefixPolicyStatement(Stack stack, String topicPrefix) {
        PolicyStatementProps iotPolicyStatementProps = PolicyStatementProps.builder()
                .effect(Effect.ALLOW)
                .resources(singletonList(Fn.join(NO_SEPARATOR, Arrays.asList("arn:aws:iot:", stack.getRegion(), ":", stack.getAccount(), TOPICFILTER, topicPrefix, ALL_SUFFIX))))
                .actions(singletonList(SharedPermissions.IOT_SUBSCRIBE_PERMISSION))
                .build();
        return new PolicyStatement(iotPolicyStatementProps);
    }

    @NotNull
    public static PolicyStatement getReceiveFromTopicPrefixPolicyStatement(Stack stack, String topicPrefix) {
        PolicyStatementProps iotPolicyStatementProps = PolicyStatementProps.builder()
                .effect(Effect.ALLOW)
                .resources(singletonList(Fn.join(NO_SEPARATOR, Arrays.asList("arn:aws:iot:", stack.getRegion(), ":", stack.getAccount(), TOPIC, topicPrefix, ALL_SUFFIX))))
                .actions(singletonList(IOT_RECEIVE_PERMISSION))
                .build();
        return new PolicyStatement(iotPolicyStatementProps);
    }

    private static final PolicyStatementProps connectAllPolicyStatementProps = PolicyStatementProps.builder()
            .effect(Effect.ALLOW)
            .resources(singletonList(ALL_RESOURCES))
            .actions(singletonList(IOT_CONNECT_PERMISSION))
            .build();
    public static final PolicyStatement connectAllPolicyStatement = new PolicyStatement(connectAllPolicyStatementProps);

    @NotNull
    public static PolicyStatement getConnectPolicyStatement(Stack stack, String clientId) {
        PolicyStatementProps iotPolicyStatementProps = PolicyStatementProps.builder()
                .effect(Effect.ALLOW)
                .resources(singletonList(Fn.join(NO_SEPARATOR, Arrays.asList("arn:aws:iot:", stack.getRegion(), ":", stack.getAccount(), "client/", clientId))))
                .actions(singletonList(IOT_CONNECT_PERMISSION))
                .build();
        return new PolicyStatement(iotPolicyStatementProps);
    }

    private static Stream<Class<IotCustomAuthorizer>> findIotCustomAuthorizersInJar(File file) {
        return findClassesInJarImplementingInterface(file, IotCustomAuthorizer.class);
    }

    private static Function createAuthorizerFunction(Stack stack, Code code, Class<IotCustomAuthorizer> clazz) {
        Role role = getRoleAssumedByLambda(stack, clazz.getSimpleName(), Optional.empty());

        String handlerName = String.join("::", clazz.getName(), HANDLE_REQUEST);

        FunctionProps functionProps = FunctionProps.builder()
                .code(code)
                .handler(handlerName)
                .memorySize(1024)
                .timeout(DEFAULT_LAMBDA_FUNCTION_TIMEOUT)
                .runtime(Runtime.JAVA_11)
                .role(role)
                .functionName(String.join("-", stack.getStackName(), clazz.getSimpleName()))
                .tracing(Tracing.ACTIVE)
                .build();

        return new Function(stack, clazz.getSimpleName(), functionProps);
    }

    private static CfnAuthorizer createIotAuthorizerFromFunction(Stack stack, Function authorizerFunction, String hash) {
        String authorizerName = "authorizer-" + hash;

        // https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-iot-authorizer-tokensigningpublickeys.html
        // NOTE: TokenSigningPublicKeysProperty is not supported so we cannot directly enable signature validation with CDK yet

        // From https://docs.aws.amazon.com/iot/latest/developerguide/custom-auth-troubleshooting.html
        CfnAuthorizerProps cfnAuthorizerProps = CfnAuthorizerProps.builder()
                .authorizerFunctionArn(authorizerFunction.getFunctionArn())
                .authorizerName(authorizerName)
                .signingDisabled(true)
                //.tokenKeyName("token")
                .status("ACTIVE")
                .build();

        CfnAuthorizer cfnAuthorizer = new CfnAuthorizer(stack, authorizerName, cfnAuthorizerProps);

        allowIotAuthorizerToInvokeLambdaFunction(stack, cfnAuthorizer, authorizerFunction, authorizerName);

        return cfnAuthorizer;
    }

    public static CfnAuthorizer getIotCustomAuthorizer(Stack stack, File file, Class<IotCustomAuthorizer> iotCustomAuthorizerClass) {
        @NotNull AssetCode assetCode = Code.fromAsset(file.getAbsolutePath());

        Function authorizerFunction = createAuthorizerFunction(stack, assetCode, iotCustomAuthorizerClass);

        return createIotAuthorizerFromFunction(stack, authorizerFunction, getFileHash(file));
    }

    private static String getFileHash(File file) {
        return Try.withResources(() -> new FileInputStream(file))
                .of(IotHelper::digest)
                .map(IotHelper::digestToString)
                // Throw an exception if this fails, we can't continue if it does
                .get();
    }

    private static String digestToString(MessageDigest messageDigest) {
        return new BigInteger(messageDigest.digest())
                // Make sure the value isn't negative
                .abs()
                // Get a base-36 value to keep it compact
                .toString(36);
    }

    private static MessageDigest digest(FileInputStream fileInputStream) {
        MessageDigest messageDigest = Try.of(() -> MessageDigest.getInstance("SHA-256")).get();

        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        while ((bytesCount = Try.of(() -> fileInputStream.read(byteArray)).get()) != -1) {
            messageDigest.update(byteArray, 0, bytesCount);
        }

        return messageDigest;
    }

    public static List<CfnAuthorizer> getIotCustomAuthorizers(Stack stack, File file) {
        return findIotCustomAuthorizersInJar(file)
                .map(clazz -> getIotCustomAuthorizer(stack, file, clazz))
                .collect(Collectors.toList());
    }
}
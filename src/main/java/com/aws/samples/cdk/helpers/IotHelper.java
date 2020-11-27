package com.aws.samples.cdk.helpers;

import com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.core.Fn;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.PolicyStatementProps;
import software.amazon.awscdk.services.iot.CfnAuthorizer;
import software.amazon.awscdk.services.iot.CfnTopicRule;
import software.amazon.awscdk.services.lambda.CfnPermission;
import software.amazon.awscdk.services.lambda.CfnPermissionProps;
import software.amazon.awscdk.services.lambda.Function;

import java.util.Arrays;

import static com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions.*;
import static com.aws.samples.cdk.helpers.CdkHelper.NO_SEPARATOR;
import static java.util.Collections.singletonList;

public class IotHelper {
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
}
package com.aws.samples.cdk.constructs.iam.permissions;

import io.vavr.control.Option;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.PolicyStatementProps;
import software.amazon.awssdk.core.SdkSystemSetting;
import software.amazon.awssdk.services.sts.StsClient;

import static java.util.Collections.singletonList;

public class SharedPermissions {
    public static final String ALL_RESOURCES = "*";
    public static final String PERMISSION_DELIMITER = ":";
    public static final String PRICING_PERMISSION_PREFIX = "pricing";
    public static final String PRICING_ALL = String.join(PERMISSION_DELIMITER, PRICING_PERMISSION_PREFIX, "*");
    private static final String DYNAMODB_PERMISSION_PREFIX = "dynamodb";
    public static final String DYNAMODB_GET_ITEM_PERMISSION = String.join(PERMISSION_DELIMITER, DYNAMODB_PERMISSION_PREFIX, "GetItem");
    public static final String DYNAMODB_PUT_ITEM_PERMISSION = String.join(PERMISSION_DELIMITER, DYNAMODB_PERMISSION_PREFIX, "PutItem");
    public static final String DYNAMODB_QUERY_PERMISSION = String.join(PERMISSION_DELIMITER, DYNAMODB_PERMISSION_PREFIX, "Query");
    public static final String DYNAMODB_SCAN_PERMISSION = String.join(PERMISSION_DELIMITER, DYNAMODB_PERMISSION_PREFIX, "Scan");
    public static final String DYNAMODB_DELETE_ITEM_PERMISSION = String.join(PERMISSION_DELIMITER, DYNAMODB_PERMISSION_PREFIX, "DeleteItem");
    private static final String LAMBDA_PERMISSION_PREFIX = "lambda";
    public static final String LAMBDA_INVOKE_FUNCTION = String.join(PERMISSION_DELIMITER, LAMBDA_PERMISSION_PREFIX, "InvokeFunction");
    private static final String IOT_PERMISSION_PREFIX = "iot";
    public static final String IOT_PUBLISH_PERMISSION = String.join(PERMISSION_DELIMITER, IOT_PERMISSION_PREFIX, "Publish");
    public static final String IOT_DESCRIBE_ENDPOINT_PERMISSION = String.join(PERMISSION_DELIMITER, IOT_PERMISSION_PREFIX, "DescribeEndpoint");
    public static final String IOT_CREATE_THING_PERMISSION = String.join(PERMISSION_DELIMITER, IOT_PERMISSION_PREFIX, "CreateThing");
    public static final String IOT_CREATE_THING_GROUP_PERMISSION = String.join(PERMISSION_DELIMITER, IOT_PERMISSION_PREFIX, "CreateThingGroup");
    public static final String IOT_UPDATE_THING_GROUPS_FOR_THING_PERMISSION = String.join(PERMISSION_DELIMITER, IOT_PERMISSION_PREFIX, "UpdateThingGroupsForThing");
    public static final String IOT_UPDATE_THING_SHADOW_PERMISSION = String.join(PERMISSION_DELIMITER, IOT_PERMISSION_PREFIX, "UpdateThingShadow");
    public static final String IOT_SEARCH_INDEX_PERMISSION = String.join(PERMISSION_DELIMITER, IOT_PERMISSION_PREFIX, "SearchIndex");
    public static final String IOT_CONNECT_PERMISSION = String.join(PERMISSION_DELIMITER, IOT_PERMISSION_PREFIX, "Connect");
    public static final String IOT_SUBSCRIBE_PERMISSION = String.join(PERMISSION_DELIMITER, IOT_PERMISSION_PREFIX, "Subscribe");
    public static final String IOT_RECEIVE_PERMISSION = String.join(PERMISSION_DELIMITER, IOT_PERMISSION_PREFIX, "Receive");
    public static final String IOT_CREATE_CERTIFICATE_FROM_CSR_PERMISSION = String.join(PERMISSION_DELIMITER, IOT_PERMISSION_PREFIX, "CreateCertificateFromCsr");
    public static final String IOT_REGISTER_CERTIFICATE_WITHOUT_CA = String.join(PERMISSION_DELIMITER, IOT_PERMISSION_PREFIX, "RegisterCertificateWithoutCa");
    public static final String IOT_UPDATE_CERTIFICATE = String.join(PERMISSION_DELIMITER, IOT_PERMISSION_PREFIX, "UpdateCertificate");
    public static final String IOT_DELETE_CERTIFICATE = String.join(PERMISSION_DELIMITER, IOT_PERMISSION_PREFIX, "DeleteCertificate");
    private static final String CLOUDWATCH_LOGS_PERMISSION_PREFIX = "logs";
    public static final String CLOUDWATCH_LOGS_CREATE_LOG_GROUP = String.join(PERMISSION_DELIMITER, CLOUDWATCH_LOGS_PERMISSION_PREFIX, "CreateLogGroup");
    public static final String CLOUDWATCH_LOGS_CREATE_LOG_STREAM = String.join(PERMISSION_DELIMITER, CLOUDWATCH_LOGS_PERMISSION_PREFIX, "CreateLogStream");
    public static final String CLOUDWATCH_LOGS_PUT_LOG_EVENTS = String.join(PERMISSION_DELIMITER, CLOUDWATCH_LOGS_PERMISSION_PREFIX, "PutLogEvents");
    public static final String CLOUDWATCH_LOGS_DESCRIBE_LOG_STREAMS = String.join(PERMISSION_DELIMITER, CLOUDWATCH_LOGS_PERMISSION_PREFIX, "DescribeLogStreams");
    private static final String STS_PERMISSION_PREFIX = "sts";
    public static final String STS_ASSUME_ROLE = String.join(PERMISSION_DELIMITER, STS_PERMISSION_PREFIX, "AssumeRole");
    public static final String STS_GET_CALLER_IDENTITY_PERMISSION = String.join(PERMISSION_DELIMITER, STS_PERMISSION_PREFIX, "GetCallerIdentity");
    public static final String STS_GET_FEDERATION_TOKEN_PERMISSION = String.join(PERMISSION_DELIMITER, STS_PERMISSION_PREFIX, "GetFederationToken");
    private static final String KINESIS_PERMISSION_PREFIX = "kinesis";
    public static final String KINESIS_PUT_RECORD = String.join(PERMISSION_DELIMITER, KINESIS_PERMISSION_PREFIX, "PutRecord");
    private static final String FIREHOSE_PERMISSION_PREFIX = "firehose";
    public static final String FIREHOSE_PUT_RECORD = String.join(PERMISSION_DELIMITER, FIREHOSE_PERMISSION_PREFIX, "PutRecord");
    private static final String IAM_PERMISSION_PREFIX = "iam";
    public static final String IAM_GET_ROLE_PERMISSION = String.join(PERMISSION_DELIMITER, IAM_PERMISSION_PREFIX, "GetRole");
    private static final String SSM_PERMISSION_PREFIX = "ssm";
    public static final String SSM_DESCRIBE_INSTANCE_INFORMATION_PERMISSION = String.join(PERMISSION_DELIMITER, SSM_PERMISSION_PREFIX, "DescribeInstanceInformation");
    public static final String SSM_DESCRIBE_ACTIVATIONS_PERMISSION = String.join(PERMISSION_DELIMITER, SSM_PERMISSION_PREFIX, "DescribeActivations");
    private static final String SQS_PERMISSION_PREFIX = "sqs";
    public static final String SQS_CHANGE_MESSAGE_VISIBILITY = String.join(PERMISSION_DELIMITER, SQS_PERMISSION_PREFIX, "ChangeMessageVisibility");
    public static final String SQS_DELETE_MESSAGE = String.join(PERMISSION_DELIMITER, SQS_PERMISSION_PREFIX, "DeleteMessage");
    public static final String SQS_GET_QUEUE_ATTRIBUTES = String.join(PERMISSION_DELIMITER, SQS_PERMISSION_PREFIX, "GetQueueAttributes");
    public static final String SQS_GET_QUEUE_URL = String.join(PERMISSION_DELIMITER, SQS_PERMISSION_PREFIX, "GetQueueUrl");
    public static final String SQS_RECEIVE_MESSAGE = String.join(PERMISSION_DELIMITER, SQS_PERMISSION_PREFIX, "ReceiveMessage");
    public static final String SQS_SEND_MESSAGE = String.join(PERMISSION_DELIMITER, SQS_PERMISSION_PREFIX, "SendMessage");

    private static Option<String> accountIdOption = Option.none();

    @NotNull
    public static PolicyStatement getAllowAllPolicyStatement(String action) {
        PolicyStatementProps policyStatementProps = PolicyStatementProps.builder()
                .effect(Effect.ALLOW)
                .resources(singletonList(ALL_RESOURCES))
                .actions(singletonList(action))
                .build();

        return new PolicyStatement(policyStatementProps);
    }

    public static String getAccountId() {
        if (accountIdOption.isEmpty()) {
            accountIdOption = Option.of(StsClient.create().getCallerIdentity().account());
        }

        return accountIdOption.get();
    }

    public static boolean isRunningInLambda() {
        return SdkSystemSetting.AWS_EXECUTION_ENV.getStringValue().isPresent();
    }
}

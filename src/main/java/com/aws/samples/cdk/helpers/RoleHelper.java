package com.aws.samples.cdk.helpers;

import com.aws.samples.cdk.constructs.iam.policies.KinesisPolicies;
import com.aws.samples.cdk.constructs.iam.policies.IotPolicies;
import com.aws.samples.cdk.constructs.iam.policies.LambdaPolicies;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.iam.*;

import static com.aws.samples.cdk.constructs.iam.policies.CloudWatchLogsPolicies.minimalCloudWatchEventsLoggingPolicy;
import static com.aws.samples.cdk.helpers.IotHelper.DESCRIBE_ENDPOINT_POLICY_STATEMENT;
import static com.aws.samples.cdk.helpers.IotHelper.getPublishToTopicPolicyStatement;

public class RoleHelper {
    public static Role buildPublishToTopicRole(Stack stack, String rolePrefix, String topic, List<PolicyStatement> policyStatements, List<IManagedPolicy> managedPolicies, IPrincipal iPrincipal) {
        PolicyStatement iotPolicyStatement = getPublishToTopicPolicyStatement(stack, topic);

        List<PolicyStatement> policyStatementList = List.of(iotPolicyStatement)
                .appendAll(policyStatements)
                .append(DESCRIBE_ENDPOINT_POLICY_STATEMENT);

        return buildRoleAssumedByPrincipal(stack, String.join("-", rolePrefix, "role"), policyStatementList, managedPolicies, iPrincipal);
    }

    public static Role buildRoleAssumedByLambda(Construct construct, String roleName, List<PolicyStatement> policyStatements, List<IManagedPolicy> managedPolicies) {
        return buildRoleAssumedByPrincipal(construct, roleName, policyStatements, managedPolicies, LambdaPolicies.LAMBDA_SERVICE_PRINCIPAL);
    }

    public static Role buildRoleAssumedByFirehose(Construct construct, String roleName, List<PolicyStatement> policyStatements, List<IManagedPolicy> managedPolicies) {
        return buildRoleAssumedByPrincipal(construct, roleName, policyStatements, managedPolicies, KinesisPolicies.FIREHOSE_SERVICE_PRINCIPAL);
    }

    public static Role buildRoleAssumedByIot(Construct construct, String roleName, List<PolicyStatement> policyStatements, List<IManagedPolicy> managedPolicies) {
        return buildRoleAssumedByPrincipal(construct, roleName, policyStatements, managedPolicies, IotPolicies.IOT_SERVICE_PRINCIPAL);
    }

    public static Role buildRoleAssumedByPrincipal(Construct construct, String roleName, List<PolicyStatement> policyStatements, List<IManagedPolicy> managedPolicies, IPrincipal iPrincipal) {
        List<PolicyStatement> allPolicyStatements = List.of(minimalCloudWatchEventsLoggingPolicy)
                .appendAll(policyStatements);

        PolicyDocumentProps policyDocumentProps = PolicyDocumentProps.builder()
                .statements(allPolicyStatements.asJava())
                .build();
        PolicyDocument policyDocument = new PolicyDocument(policyDocumentProps);

        Map<String, PolicyDocument> policyDocuments = HashMap.of("root", policyDocument);

        RoleProps roleProps = RoleProps.builder()
                .assumedBy(iPrincipal)
                .inlinePolicies(policyDocuments.toJavaMap())
                .managedPolicies(managedPolicies.asJava())
                .build();

        return new Role(construct, roleName, roleProps);
    }

    public static Role buildPublishToTopicPrefixIotEventRole(Stack stack, String rolePrefix, String topicPrefix, List<PolicyStatement> policyStatements, List<IManagedPolicy> managedPolicies, IPrincipal iPrincipal) {
        return buildPublishToTopicRole(stack, rolePrefix, topicPrefix + "/*", policyStatements, managedPolicies, iPrincipal);
    }
}

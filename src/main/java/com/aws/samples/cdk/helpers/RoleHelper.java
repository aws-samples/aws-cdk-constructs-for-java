package com.aws.samples.cdk.helpers;

import com.aws.samples.cdk.constructs.iam.policies.IotPolicies;
import com.aws.samples.cdk.constructs.iam.policies.LambdaPolicies;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.iam.*;

import java.util.*;

import static com.aws.samples.cdk.constructs.iam.policies.CloudWatchLogsPolicies.minimalCloudWatchEventsLoggingPolicy;
import static com.aws.samples.cdk.helpers.IotHelper.getPublishToTopicPolicyStatement;
import static java.util.Collections.singletonList;

public class RoleHelper {
    public static Role buildPublishToTopicRole(Stack stack, String rolePrefix, String topic, Optional<List<PolicyStatement>> optionalPolicyStatements, Optional<List<ManagedPolicy>> optionalManagedPolicies, IPrincipal iPrincipal) {
        PolicyStatement iotPolicyStatement = getPublishToTopicPolicyStatement(stack, topic);

        return buildRoleAssumedByPrincipal(stack, rolePrefix + "Role", Optional.of(combinePolicyStatements(optionalPolicyStatements, iotPolicyStatement)), optionalManagedPolicies, iPrincipal);
    }

    public static Role buildRoleAssumedByLambda(Construct construct, String roleName, Optional<List<PolicyStatement>> optionalPolicyStatements, Optional<List<ManagedPolicy>> optionalManagedPolicies) {
        return buildRoleAssumedByPrincipal(construct, roleName, optionalPolicyStatements, optionalManagedPolicies, LambdaPolicies.LAMBDA_SERVICE_PRINCIPAL);
    }

    public static Role buildRoleAssumedByIot(Construct construct, String roleName, Optional<List<PolicyStatement>> optionalPolicyStatements, Optional<List<ManagedPolicy>> optionalManagedPolicies) {
        return buildRoleAssumedByPrincipal(construct, roleName, optionalPolicyStatements, optionalManagedPolicies, IotPolicies.IOT_SERVICE_PRINCIPAL);
    }

    public static Role buildRoleAssumedByPrincipal(Construct construct, String roleName, Optional<List<PolicyStatement>> optionalPolicyStatements, Optional<List<ManagedPolicy>> optionalManagedPolicies, IPrincipal iPrincipal) {
        List<PolicyStatement> basePolicyStatements = singletonList(minimalCloudWatchEventsLoggingPolicy);

        List<PolicyStatement> allPolicyStatements = new ArrayList<>();
        allPolicyStatements.addAll(basePolicyStatements);
        optionalPolicyStatements.ifPresent(allPolicyStatements::addAll);

        PolicyDocumentProps policyDocumentProps = PolicyDocumentProps.builder()
                .statements(allPolicyStatements)
                .build();
        PolicyDocument policyDocument = new PolicyDocument(policyDocumentProps);

        Map<String, PolicyDocument> policyDocuments = new HashMap<>();
        policyDocuments.put("root", policyDocument);

        RoleProps roleProps = RoleProps.builder()
                .assumedBy(iPrincipal)
                .inlinePolicies(policyDocuments)
                .managedPolicies(optionalManagedPolicies.orElse(new ArrayList<>()))
                .build();

        return new Role(construct, roleName, roleProps);
    }

    @NotNull
    public static List<PolicyStatement> combinePolicyStatements(Optional<List<PolicyStatement>> optionalPolicyStatements, PolicyStatement policyStatement) {
        List<PolicyStatement> allStatements = new ArrayList<>();
        optionalPolicyStatements.ifPresent(allStatements::addAll);
        allStatements.add(policyStatement);
        return allStatements;
    }

    public static Role buildPublishToTopicPrefixIotEventRole(Stack stack, String rolePrefix, String topicPrefix, Optional<List<PolicyStatement>> optionalPolicyStatements, Optional<List<ManagedPolicy>> optionalManagedPolicies, IPrincipal iPrincipal) {
        return buildPublishToTopicRole(stack, rolePrefix, topicPrefix + "/*", optionalPolicyStatements, optionalManagedPolicies, iPrincipal);
    }
}

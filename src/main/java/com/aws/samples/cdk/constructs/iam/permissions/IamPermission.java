package com.aws.samples.cdk.constructs.iam.permissions;

import io.vavr.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.PolicyStatementProps;

import static java.util.Collections.singletonList;

public interface IamPermission {
    Logger log = LoggerFactory.getLogger(IamPermission.class);

    Tuple2<IamAction, IamResource> getActionAndResource();

    default PolicyStatement getPolicyStatement() {
        return new PolicyStatement(
                PolicyStatementProps.builder()
                        .effect(Effect.ALLOW)
                        .actions(singletonList(getActionAndResource()._1.getIamString()))
                        .resources(singletonList(getActionAndResource()._2.getIamString()))
                        .build());
    }
}

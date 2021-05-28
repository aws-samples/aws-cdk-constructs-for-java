package com.aws.samples.cdk.constructs.iam.policies;

import com.aws.samples.cdk.constructs.iam.permissions.kinesis.resources.KinesisFirehoseStream;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.PolicyStatementProps;
import software.amazon.awscdk.services.iam.ServicePrincipal;

import java.util.Collections;

import static com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions.KINESIS_PUT_RECORD;

public class KinesisPolicies {
    public static final ServicePrincipal FIREHOSE_SERVICE_PRINCIPAL = new ServicePrincipal("firehose");
    public static final ServicePrincipal KINESIS_SERVICE_PRINCIPAL = new ServicePrincipal("kinesis");

    public static PolicyStatement getPutRecordPolicyStatement(KinesisFirehoseStream kinesisStream) {
        PolicyStatementProps kinesisStreamPolicyStatementProps = PolicyStatementProps.builder()
                .effect(Effect.ALLOW)
                .resources(Collections.singletonList(kinesisStream.getIamString()))
                .actions(Collections.singletonList(KINESIS_PUT_RECORD))
                .build();

        return new PolicyStatement(kinesisStreamPolicyStatementProps);
    }
}

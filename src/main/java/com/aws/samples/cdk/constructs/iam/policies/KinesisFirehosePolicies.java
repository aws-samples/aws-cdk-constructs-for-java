package com.aws.samples.cdk.constructs.iam.policies;

import com.aws.samples.cdk.constructs.iam.permissions.kinesisfirehose.kinesis.resources.KinesisFirehoseStream;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.PolicyStatementProps;
import software.amazon.awscdk.services.iam.ServicePrincipal;

import java.util.Collections;

import static com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions.FIREHOSE_PUT_RECORD;

public class KinesisFirehosePolicies {
    public static final ServicePrincipal FIREHOSE_SERVICE_PRINCIPAL = new ServicePrincipal("firehose");

    public static PolicyStatement getKinesisFirehosePutRecordPolicyStatement(KinesisFirehoseStream kinesisFirehoseStream) {
        PolicyStatementProps kinesisStreamPolicyStatementProps = PolicyStatementProps.builder()
                .effect(Effect.ALLOW)
                .resources(Collections.singletonList(kinesisFirehoseStream.getIamString()))
                .actions(Collections.singletonList(FIREHOSE_PUT_RECORD))
                .build();

        return new PolicyStatement(kinesisStreamPolicyStatementProps);
    }
}

package com.aws.samples.cdk.constructs.iam.permissions.lambda;

import com.aws.samples.cdk.constructs.iam.permissions.lambda.actions.ImmutableInvokeAll;
import com.aws.samples.cdk.constructs.iam.permissions.lambda.actions.InvokeAll;

public class LambdaActions {
    public static InvokeAll invokeAll = ImmutableInvokeAll.builder().build();
}

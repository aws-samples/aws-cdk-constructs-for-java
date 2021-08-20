package com.aws.samples.cdk.constructs.iam.permissions.lambda;

import com.aws.samples.cdk.constructs.iam.permissions.lambda.actions.ImmutableInvoke;
import com.aws.samples.cdk.constructs.iam.permissions.lambda.actions.ImmutableInvokeAll;
import com.aws.samples.cdk.constructs.iam.permissions.lambda.actions.Invoke;
import com.aws.samples.cdk.constructs.iam.permissions.lambda.actions.InvokeAll;
import com.aws.samples.cdk.constructs.iam.permissions.lambda.resources.Function;

public class LambdaActions {
    public static InvokeAll invokeAll = ImmutableInvokeAll.builder().build();

    public static Invoke invoke(Function function) {
        return ImmutableInvoke.builder().function(function).build();
    }
}

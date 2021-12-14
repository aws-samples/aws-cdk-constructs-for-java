package com.aws.samples.cdk.constructs.iam.permissions.lambda;

import com.aws.samples.cdk.constructs.iam.permissions.lambda.actions.Invoke;
import com.aws.samples.cdk.constructs.iam.permissions.lambda.actions.InvokeAll;
import com.aws.samples.cdk.constructs.iam.permissions.lambda.resources.Function;

public class LambdaActions {
    public static InvokeAll invokeAll = InvokeAll.builder().build();

    public static Invoke invoke(Function function) {
        return Invoke.builder().function(function).build();
    }
}

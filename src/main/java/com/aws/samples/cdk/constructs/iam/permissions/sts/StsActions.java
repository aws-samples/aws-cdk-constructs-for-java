package com.aws.samples.cdk.constructs.iam.permissions.sts;

import com.aws.samples.cdk.constructs.iam.permissions.sts.actions.GetCallerIdentity;
import com.aws.samples.cdk.constructs.iam.permissions.sts.actions.GetFederationToken;

public class StsActions {
    public static GetCallerIdentity getCallerIdentity = GetCallerIdentity.builder().build();

    public static GetFederationToken getFederationToken = GetFederationToken.builder().build();
}

package com.aws.samples.cdk.constructs.iam.permissions.sts;

import com.aws.samples.cdk.constructs.iam.permissions.sts.actions.GetCallerIdentity;
import com.aws.samples.cdk.constructs.iam.permissions.sts.actions.GetFederationToken;
import com.aws.samples.cdk.constructs.iam.permissions.sts.actions.ImmutableGetCallerIdentity;
import com.aws.samples.cdk.constructs.iam.permissions.sts.actions.ImmutableGetFederationToken;

public class StsActions {
    public static GetCallerIdentity getCallerIdentity = ImmutableGetCallerIdentity.builder().build();

    public static GetFederationToken getFederationToken = ImmutableGetFederationToken.builder().build();
}

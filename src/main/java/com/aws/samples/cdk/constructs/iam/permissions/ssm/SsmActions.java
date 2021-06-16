package com.aws.samples.cdk.constructs.iam.permissions.ssm;

import com.aws.samples.cdk.constructs.iam.permissions.ssm.actions.DescribeInstanceInformation;
import com.aws.samples.cdk.constructs.iam.permissions.ssm.actions.ImmutableDescribeInstanceInformation;

public class SsmActions {
    public static DescribeInstanceInformation describeInstanceInformation = ImmutableDescribeInstanceInformation.builder().build();
}

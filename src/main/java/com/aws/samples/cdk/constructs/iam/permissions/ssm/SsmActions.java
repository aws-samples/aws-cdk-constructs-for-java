package com.aws.samples.cdk.constructs.iam.permissions.ssm;

import com.aws.samples.cdk.constructs.iam.permissions.ssm.actions.DescribeActivations;
import com.aws.samples.cdk.constructs.iam.permissions.ssm.actions.DescribeInstanceInformation;

public class SsmActions {
    public static DescribeInstanceInformation describeInstanceInformation = DescribeInstanceInformation.builder().build();
    public static DescribeActivations describeActivations = DescribeActivations.builder().build();
}

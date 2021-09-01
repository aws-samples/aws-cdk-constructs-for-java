package com.aws.samples.cdk.constructs.iam.policies;

import io.vavr.collection.List;
import software.amazon.awscdk.services.iam.*;

import static com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions.*;

public class CloudWatchPolicies {
    private static final String CLOUD_WATCH_FULL_ACCESS_POLICY_NAME = "CloudWatchFullAccess";
    public static final IManagedPolicy CLOUD_WATCH_FULL_ACCESS_POLICY = ManagedPolicy.fromAwsManagedPolicyName(CLOUD_WATCH_FULL_ACCESS_POLICY_NAME);
}

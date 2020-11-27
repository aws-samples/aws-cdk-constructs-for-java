package com.aws.samples.cdk.helpers;

import com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions;
import software.amazon.awscdk.services.iam.PolicyStatement;

import static com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions.PRICING_ALL;

public class PricingHelper {
    public static PolicyStatement allowAllPricingActionsStatement = SharedPermissions.getAllowAllPolicyStatement(PRICING_ALL);
}
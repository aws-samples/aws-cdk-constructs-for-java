package com.aws.samples.cdk.constructs.iot.authorizer.data.output;

import java.util.List;

public class AuthorizationResponse {
    public boolean isAuthenticated;
    public String principalId;
    public int disconnectAfterInSeconds;
    public int refreshAfterInSeconds;
    public List<PolicyDocument> policyDocuments;
}

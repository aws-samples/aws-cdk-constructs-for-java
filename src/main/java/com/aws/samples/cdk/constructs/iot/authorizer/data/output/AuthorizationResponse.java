package com.aws.samples.cdk.constructs.iot.authorizer.data.output;

// NOTE: Do not change this to io.vavr.collection.List! See comment below.

import java.util.List;

public class AuthorizationResponse {
    public boolean isAuthenticated;
    public String principalId;
    public int disconnectAfterInSeconds;
    public int refreshAfterInSeconds;
    // This must be a java.util.List, not an io.vavr.collection.List, so serialization to JSON in Lambda works
    public List<PolicyDocument> policyDocuments;
}

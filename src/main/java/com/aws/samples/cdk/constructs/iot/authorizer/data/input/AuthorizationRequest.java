package com.aws.samples.cdk.constructs.iot.authorizer.data.input;

// NOTE: Do not change this to io.vavr.collection.List! See comment below.

import java.util.List;

public class AuthorizationRequest {
    public String token;
    public boolean signatureVerified;
    // This must be a java.util.List, not an io.vavr.collection.List, so de-serialization from JSON in Lambda works
    public List<String> protocols;
    public ProtocolData protocolData;
    public ConnectionMetadata connectionMetadata;
}

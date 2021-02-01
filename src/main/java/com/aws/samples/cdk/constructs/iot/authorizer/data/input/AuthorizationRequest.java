package com.aws.samples.cdk.constructs.iot.authorizer.data.input;

import io.vavr.collection.List;

public class AuthorizationRequest {
    public String token;
    public boolean signatureVerified;
    public List<String> protocols;
    public ProtocolData protocolData;
    public ConnectionMetadata connectionMetadata;
}

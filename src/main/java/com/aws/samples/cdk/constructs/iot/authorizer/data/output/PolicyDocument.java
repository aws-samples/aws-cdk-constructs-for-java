package com.aws.samples.cdk.constructs.iot.authorizer.data.output;

// NOTE: Do not change this to io.vavr.collection.List! See comment below.

import java.util.List;

public class PolicyDocument {
    public String Version = "2012-10-17";
    // This must be a java.util.List, not an io.vavr.collection.List, so serialization to JSON in Lambda works
    public List<Statement> Statement;
}

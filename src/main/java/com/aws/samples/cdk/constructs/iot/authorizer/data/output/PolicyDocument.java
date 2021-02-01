package com.aws.samples.cdk.constructs.iot.authorizer.data.output;

import io.vavr.collection.List;

public class PolicyDocument {
    public String Version = "2012-10-17";
    public List<Statement> Statement;
}

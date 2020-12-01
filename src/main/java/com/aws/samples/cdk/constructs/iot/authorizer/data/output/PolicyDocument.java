package com.aws.samples.cdk.constructs.iot.authorizer.data.output;

import java.util.List;

public class PolicyDocument {
    public String Version = "2012-10-17";
    public List<Statement> Statement;
}

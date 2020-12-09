package com.aws.samples.cdk.helpers.data;

import com.aws.samples.lambda.servlet.automation.GeneratedClassInfo;
import software.amazon.awscdk.services.lambda.Function;

public class AwsLambdaServlet {
    public final GeneratedClassInfo generatedClassInfo;
    public final Function function;

    public AwsLambdaServlet(GeneratedClassInfo generatedClassInfo, Function function) {
        this.generatedClassInfo = generatedClassInfo;
        this.function = function;
    }
}

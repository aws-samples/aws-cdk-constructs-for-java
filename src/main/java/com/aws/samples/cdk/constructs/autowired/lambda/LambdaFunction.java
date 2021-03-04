package com.aws.samples.cdk.constructs.autowired.lambda;

import com.amazonaws.services.lambda.runtime.RequestHandler;

public interface LambdaFunction<I, O> extends RequestHandler<I, O> {
}

package com.aws.samples.cdk.constructs.iot.authorizer;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.aws.samples.cdk.constructs.iot.authorizer.data.input.AuthorizationRequest;
import com.aws.samples.cdk.constructs.iot.authorizer.data.output.AuthorizationResponse;

public interface IotCustomAuthorizer extends RequestHandler<AuthorizationRequest, AuthorizationResponse> {
}
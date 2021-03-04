package com.aws.samples.cdk.constructs.autowired.iot;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.aws.samples.cdk.constructs.iot.authorizer.data.TokenSigningConfiguration;
import com.aws.samples.cdk.constructs.iot.authorizer.data.input.AuthorizationRequest;
import com.aws.samples.cdk.constructs.iot.authorizer.data.output.AuthorizationResponse;
import io.vavr.control.Option;

public interface IotCustomAuthorizer extends RequestHandler<AuthorizationRequest, AuthorizationResponse> {
    Option<TokenSigningConfiguration> getTokenSigningConfigurationOption();
}

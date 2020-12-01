package com.aws.samples.cdk.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This annotation should be added to a class that is an IoT custom authorizer that should be configured as the default authorizer
 */
@Target({ElementType.TYPE})
public @interface IotDefaultAuthorizer {
}
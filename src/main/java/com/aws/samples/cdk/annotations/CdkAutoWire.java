package com.aws.samples.cdk.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This annotation should be added to a class that should be inspected by the CDK auto-wiring code. These classes are
 * in the com.aws.samples.cdk.constructs.autowired package.
 */
@Target({ElementType.TYPE})
public @interface CdkAutoWire {
}

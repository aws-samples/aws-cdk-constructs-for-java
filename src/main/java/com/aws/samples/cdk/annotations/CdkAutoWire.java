package com.aws.samples.cdk.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
public @interface CdkAutoWire {
}

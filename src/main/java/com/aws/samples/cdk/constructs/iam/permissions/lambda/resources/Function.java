package com.aws.samples.cdk.constructs.iam.permissions.lambda.resources;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class Function implements LambdaResource {
    public static Builder builder() {
        return new Builder();
    }

    public abstract String getFunctionName();

    @Override
    public String getResourceType() {
        return "function";
    }

    @Override
    public String getResourceValue() {
        return getFunctionName();
    }

    public static class Builder extends ImmutableFunction.Builder {
    }
}

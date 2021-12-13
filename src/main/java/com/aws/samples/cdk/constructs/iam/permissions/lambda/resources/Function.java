package com.aws.samples.cdk.constructs.iam.permissions.lambda.resources;

import com.aws.samples.cdk.constructs.iam.permissions.sqs.actions.GetQueueUrl;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class Function implements LambdaResource {
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

    public static Builder builder() {
        return new Builder();
    }
}

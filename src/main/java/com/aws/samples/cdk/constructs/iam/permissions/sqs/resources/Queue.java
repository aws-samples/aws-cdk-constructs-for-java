package com.aws.samples.cdk.constructs.iam.permissions.sqs.resources;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class Queue implements SqsResource {
    public abstract String getQueueName();

    /**
     * Set this to the account ID that owns the queue. If the queue is in this account this call can be omitted.
     *
     * @return the account ID that the SQS queue exists in
     */
    @Value.Default
    public String getAccountId() {
        return SqsResource.super.getAccountId();
    }

    @Override
    public String getResourceType() {
        return "";
    }

    @Override
    public String getResourceValue() {
        return getQueueName();
    }

    public static class Builder extends ImmutableQueue.Builder {
    }

    public static Builder builder() {
        return new Builder();
    }
}

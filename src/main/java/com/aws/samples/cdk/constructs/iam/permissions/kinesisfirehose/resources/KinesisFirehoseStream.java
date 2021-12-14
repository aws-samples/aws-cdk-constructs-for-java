package com.aws.samples.cdk.constructs.iam.permissions.kinesisfirehose.resources;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class KinesisFirehoseStream implements KinesisFirehoseResource {
    public static Builder builder() {
        return new Builder();
    }

    public abstract String getName();

    @Override
    public String getResourceType() {
        return "deliverystream";
    }

    @Override
    public String getResourceValue() {
        return getName();
    }

    public static class Builder extends ImmutableKinesisFirehoseStream.Builder {
    }
}

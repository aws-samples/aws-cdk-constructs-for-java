package com.aws.samples.cdk.constructs.iam.permissions.kinesis.resources;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class KinesisStream implements KinesisResource {
    public abstract String getName();

    @Override
    public String getResourceType() {
        return "stream";
    }

    @Override
    public String getResourceValue() {
        return getName();
    }
}

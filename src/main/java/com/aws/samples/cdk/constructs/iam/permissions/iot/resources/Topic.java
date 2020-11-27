package com.aws.samples.cdk.constructs.iam.permissions.iot.resources;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class Topic implements IotResource {
    public abstract String getTopic();

    @Override
    public String getResourceType() {
        return "topic";
    }

    @Override
    public String getResourceValue() {
        return getTopic();
    }
}

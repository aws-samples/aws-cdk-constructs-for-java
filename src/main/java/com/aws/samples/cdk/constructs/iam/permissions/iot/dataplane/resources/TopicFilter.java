package com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class TopicFilter implements IotResource {
    public static Builder builder() {
        return new Builder();
    }

    public abstract String getTopicFilter();

    @Override
    public String getResourceType() {
        return "topicfilter";
    }

    @Override
    public String getResourceValue() {
        return getTopicFilter();
    }

    public static class Builder extends ImmutableTopicFilter.Builder {
    }
}

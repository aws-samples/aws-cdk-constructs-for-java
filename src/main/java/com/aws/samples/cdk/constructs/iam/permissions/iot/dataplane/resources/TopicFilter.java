package com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources;

import com.aws.samples.cdk.constructs.iam.permissions.sqs.actions.GetQueueUrl;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class TopicFilter implements IotResource {
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

    public static Builder builder() {
        return new Builder();
    }
}

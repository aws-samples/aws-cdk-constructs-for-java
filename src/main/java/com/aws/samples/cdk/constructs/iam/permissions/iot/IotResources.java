package com.aws.samples.cdk.constructs.iam.permissions.iot;

import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.*;

public class IotResources {
    public static Topic topic(String topic) {
        return ImmutableTopic.builder().topic(topic).build();
    }

    public static TopicFilter topicFilter(String topicFilter) {
        return ImmutableTopicFilter.builder().topicFilter(topicFilter).build();
    }

    public static ClientId clientId(String clientId) {
        return ImmutableClientId.builder().clientId(clientId).build();
    }
}

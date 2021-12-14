package com.aws.samples.cdk.constructs.iam.permissions.iot;

import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.ClientId;
import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.Topic;
import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.TopicFilter;

public class IotResources {
    public static Topic topic(String topic) {
        return Topic.builder().topic(topic).build();
    }

    public static TopicFilter topicFilter(String topicFilter) {
        return TopicFilter.builder().topicFilter(topicFilter).build();
    }

    public static ClientId clientId(String clientId) {
        return ClientId.builder().clientId(clientId).build();
    }
}

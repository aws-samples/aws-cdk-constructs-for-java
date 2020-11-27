package com.aws.samples.cdk.constructs.iam.permissions.iot;

import com.aws.samples.cdk.constructs.iam.permissions.iot.resources.Topic;
import com.aws.samples.cdk.constructs.iam.permissions.iot.resources.TopicFilter;
import com.aws.samples.cdk.constructs.iam.permissions.iot.resources.ImmutableTopic;
import com.aws.samples.cdk.constructs.iam.permissions.iot.resources.ImmutableTopicFilter;

public class IotResources {
    public static Topic topic(String topic) {
        return ImmutableTopic.builder().topic(topic).build();
    }

    public static TopicFilter topicFilter(String topicFilter) {
        return ImmutableTopicFilter.builder().topicFilter(topicFilter).build();
    }
}

package com.aws.samples.cdk.constructs.iam.permissions.iot;

import com.aws.samples.cdk.constructs.iam.permissions.iot.actions.*;
import com.aws.samples.cdk.constructs.iam.permissions.iot.resources.ClientId;
import com.aws.samples.cdk.constructs.iam.permissions.iot.resources.Topic;
import com.aws.samples.cdk.constructs.iam.permissions.iot.resources.TopicFilter;

public class IotActions {
    public static Publish publish(Topic topic) {
        return ImmutablePublish.builder().topic(topic).build();
    }

    public static Receive receive(Topic topic) {
        return ImmutableReceive.builder().topic(topic).build();
    }

    public static Subscribe subscribe(TopicFilter topicFilter) {
        return ImmutableSubscribe.builder().topicFilter(topicFilter).build();
    }

    public static DescribeEndpoint describeEndpoint = ImmutableDescribeEndpoint.builder().build();

    public static Connect connect(ClientId clientId) {
        return ImmutableConnect.builder().clientId(clientId).build();
    }
}

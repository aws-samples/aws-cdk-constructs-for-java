package com.aws.samples.cdk.constructs.iam.permissions.iot;

import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.actions.*;
import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.ClientId;
import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.Topic;
import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.TopicFilter;

public class IotActions {
    public static DescribeEndpoint describeEndpoint = ImmutableDescribeEndpoint.builder().build();

    public static Publish publish(Topic topic) {
        return ImmutablePublish.builder().topic(topic).build();
    }

    public static Receive receive(Topic topic) {
        return ImmutableReceive.builder().topic(topic).build();
    }

    public static Subscribe subscribe(TopicFilter topicFilter) {
        return ImmutableSubscribe.builder().topicFilter(topicFilter).build();
    }

    public static Connect connect(ClientId clientId) {
        return ImmutableConnect.builder().clientId(clientId).build();
    }
}

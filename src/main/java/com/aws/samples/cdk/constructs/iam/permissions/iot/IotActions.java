package com.aws.samples.cdk.constructs.iam.permissions.iot;

import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.actions.*;
import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.ClientId;
import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.Topic;
import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.TopicFilter;

public class IotActions {
    public static DescribeEndpoint describeEndpoint = DescribeEndpoint.builder().build();

    public static SearchIndex searchIndex = SearchIndex.builder().build();

    public static Publish publish(Topic topic) {
        return Publish.builder().topic(topic).build();
    }

    public static Receive receive(Topic topic) {
        return Receive.builder().topic(topic).build();
    }

    public static Subscribe subscribe(TopicFilter topicFilter) {
        return Subscribe.builder().topicFilter(topicFilter).build();
    }

    public static Connect connect(ClientId clientId) {
        return Connect.builder().clientId(clientId).build();
    }
}

package com.aws.samples.cdk.constructs.iam.permissions.sqs;

import com.aws.samples.cdk.constructs.iam.permissions.sqs.actions.*;
import com.aws.samples.cdk.constructs.iam.permissions.sqs.resources.Queue;

public class SqsActions {
    public static ChangeMessageVisibility changeMessageVisibility(Queue queue) {
        return ImmutableChangeMessageVisibility.builder().queue(queue).build();
    }

    public static DeleteMessage deleteMessage(Queue queue) {
        return ImmutableDeleteMessage.builder().queue(queue).build();
    }

    public static GetQueueAttributes getQueueAttributes(Queue queue) {
        return ImmutableGetQueueAttributes.builder().queue(queue).build();
    }

    public static GetQueueUrl getQueueUrl(Queue queue) {
        return ImmutableGetQueueUrl.builder().queue(queue).build();
    }

    public static ReceiveMessage receiveMessage(Queue queue) {
        return ImmutableReceiveMessage.builder().queue(queue).build();
    }

    public static SendMessage sendMessage(Queue queue) {
        return ImmutableSendMessage.builder().queue(queue).build();
    }
}

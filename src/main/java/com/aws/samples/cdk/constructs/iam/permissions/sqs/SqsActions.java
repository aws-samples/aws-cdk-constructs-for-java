package com.aws.samples.cdk.constructs.iam.permissions.sqs;

import com.aws.samples.cdk.constructs.iam.permissions.sqs.actions.*;
import com.aws.samples.cdk.constructs.iam.permissions.sqs.resources.Queue;

public class SqsActions {
    public static ChangeMessageVisibility changeMessageVisibility(Queue queue) {
        return ChangeMessageVisibility.builder().queue(queue).build();
    }

    public static DeleteMessage deleteMessage(Queue queue) {
        return DeleteMessage.builder().queue(queue).build();
    }

    public static GetQueueAttributes getQueueAttributes(Queue queue) {
        return GetQueueAttributes.builder().queue(queue).build();
    }

    public static GetQueueUrl getQueueUrl(Queue queue) {
        return GetQueueUrl.builder().queue(queue).build();
    }

    public static ReceiveMessage receiveMessage(Queue queue) {
        return ReceiveMessage.builder().queue(queue).build();
    }

    public static SendMessage sendMessage(Queue queue) {
        return SendMessage.builder().queue(queue).build();
    }
}

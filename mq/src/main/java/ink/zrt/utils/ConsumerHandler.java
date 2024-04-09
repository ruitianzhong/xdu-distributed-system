package ink.zrt.utils;

import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.message.MessageView;

public interface ConsumerHandler {
    ConsumeResult handle(MessageView mv);
}

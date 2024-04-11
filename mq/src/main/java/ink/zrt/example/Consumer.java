package ink.zrt.example;

import ink.zrt.utils.ConsumerHandler;
import ink.zrt.utils.WrappedConsumer;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.message.MessageView;

public class Consumer implements ConsumerHandler {
    public static void main(String[] args) throws ClientException, InterruptedException {
        WrappedConsumer wrappedConsumer = new WrappedConsumer("test", "test_group_2", new Consumer());
        // When consumer grouped is changed, all the messages are consumed from the start.
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public ConsumeResult handle(MessageView mv) {
        byte[] b = new byte[mv.getBody().limit()];
        mv.getBody().get(b);
        System.out.println(new String(b));
        return ConsumeResult.SUCCESS;
    }
}

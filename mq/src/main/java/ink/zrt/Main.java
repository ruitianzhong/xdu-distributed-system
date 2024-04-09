package ink.zrt;

import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;

import java.io.IOException;
import java.util.Collections;


public class Main {

    public static void main(String[] args) throws ClientException, IOException, InterruptedException {
        String endpoints = "localhost:8081";
        final ClientServiceProvider provider = ClientServiceProvider.loadService();
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
                .setEndpoints(endpoints)
                .build();
        String tag = "*";
        FilterExpression filterExpression = new FilterExpression(tag, FilterExpressionType.TAG);
        String consumerGroup = "group1";
        String topic = "device";
        System.out.println("testing");

        PushConsumer pushConsumer = provider.newPushConsumerBuilder()
                .setClientConfiguration(clientConfiguration)
                .setConsumerGroup(consumerGroup)
                .setSubscriptionExpressions(Collections.singletonMap(topic, filterExpression))
                .setMessageListener(messageView -> {

                    System.out.println(messageView);
                    byte[] b = new byte[messageView.getBody().limit()];
                    messageView.getBody().get(b);
                    String s = new String(b);
                    System.out.println(s);
                    System.out.println(messageView.getMessageGroup());
                    return ConsumeResult.SUCCESS;
                })
                .build();
        System.out.println(pushConsumer.getConsumerGroup());
        Thread.sleep(Long.MAX_VALUE);
        pushConsumer.close();
    }
}
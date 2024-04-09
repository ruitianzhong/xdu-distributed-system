package ink.zrt.utils;

import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;

import java.io.IOException;
import java.util.Collections;

public class WrappedConsumer {
    private final PushConsumer pushConsumer;
    private final ConsumerHandler consumerHandler;

    public WrappedConsumer(String topic, String consumerGroup, ConsumerHandler ch) throws ClientException {
        String endpoints = "localhost:8081";
        this.consumerHandler = ch;
        final ClientServiceProvider provider = ClientServiceProvider.loadService();
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
                .setEndpoints(endpoints)
                .build();
        String tag = "*";
        FilterExpression filterExpression = new FilterExpression(tag, FilterExpressionType.TAG);
        System.out.println("Starting Consumer");

        pushConsumer = provider.newPushConsumerBuilder()
                .setClientConfiguration(clientConfiguration)
                .setConsumerGroup(consumerGroup)
                .setSubscriptionExpressions(Collections.singletonMap(topic, filterExpression))
                .setMessageListener(consumerHandler::handle)
                .build();
    }

    public void close() throws IOException {
        pushConsumer.close();
    }

}

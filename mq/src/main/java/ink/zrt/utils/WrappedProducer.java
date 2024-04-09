package ink.zrt.utils;

import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientConfigurationBuilder;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.SendReceipt;

import java.io.IOException;

public class WrappedProducer {
    private final org.apache.rocketmq.client.apis.producer.Producer producer;
    private final ClientServiceProvider provider;
    private final String topic;

    public WrappedProducer(String topic) throws ClientException {
        String endpoint = "localhost:8081";
        provider = ClientServiceProvider.loadService();
        ClientConfigurationBuilder builder = ClientConfiguration.newBuilder().setEndpoints(endpoint);
        ClientConfiguration configuration = builder.build();
        producer = provider.newProducerBuilder()
                .setTopics(topic)
                .setClientConfiguration(configuration)
                .build();
        this.topic = topic;
    }

    public void sendMessage(String msg) {
        Message message = provider.newMessageBuilder()
                .setTopic(topic)
                .setBody(msg.getBytes())
                .build();
        try {
            SendReceipt sendReceipt = producer.send(message);
        } catch (ClientException e) {
            System.out.println(e.getMessage());
        }
    }

    public void close() throws IOException {
        producer.close();
    }
}

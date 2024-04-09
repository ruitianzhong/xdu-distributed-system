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

    public WrappedProducer(String topic) throws ClientException {
        String endpoint = "localhost:8081";
        // 消息发送的目标Topic名称，需要提前创建。
        provider = ClientServiceProvider.loadService();
        ClientConfigurationBuilder builder = ClientConfiguration.newBuilder().setEndpoints(endpoint);
        ClientConfiguration configuration = builder.build();
        // 初始化Producer时需要设置通信配置以及预绑定的Topic。
        producer = provider.newProducerBuilder()
                .setTopics(topic)
                .setClientConfiguration(configuration)
                .build();
    }

    public void sendMessage(String msg) {
        Message message = provider.newMessageBuilder()
                .setTopic("device")
                .setBody(msg.getBytes())
                .build();
        try {
            SendReceipt sendReceipt = producer.send(message);
            System.out.println(sendReceipt.getMessageId());
        } catch (ClientException e) {
            System.out.println(e.getMessage());
        }
    }

    public void close() throws IOException {
        producer.close();
    }
}

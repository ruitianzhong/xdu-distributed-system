package ink.zrt.example;

import ink.zrt.utils.WrappedProducer;
import org.apache.rocketmq.client.apis.ClientException;

import java.io.IOException;

public class Producer {
    public static void main(String[] args) throws ClientException, IOException {
        WrappedProducer wrappedProducer = new WrappedProducer("test");
        for (int i = 0; i < 10; i++) {
            wrappedProducer.sendMessage(String.valueOf(i));
        }
        wrappedProducer.close();
    }
}

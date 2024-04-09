package ink.zrt.device;

import com.google.gson.Gson;
import ink.zrt.Message.DeviceMessage;
import ink.zrt.utils.WrappedProducer;
import org.apache.commons.math3.distribution.NormalDistribution;

import org.apache.rocketmq.client.apis.ClientException;

import java.io.IOException;

public class Device {

    private final int id;
    private final WrappedProducer producer;

    public Device(int id) throws ClientException {
        this.id = id;
        this.producer = new WrappedProducer("device");
    }

    public void run() throws IOException {
        NormalDistribution normalDistribution = new NormalDistribution(id, id);
        Gson gson = new Gson();
        DeviceMessage deviceMessage = new DeviceMessage();


        for (; ; ) {
            deviceMessage.data = normalDistribution.sample();
            deviceMessage.id = String.valueOf(id);
            String msg = gson.toJson(deviceMessage);
            producer.sendMessage(msg);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
        producer.close();
    }


}

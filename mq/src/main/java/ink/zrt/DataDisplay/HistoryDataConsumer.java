package ink.zrt.DataDisplay;

import com.google.gson.Gson;
import ink.zrt.Message.DeviceMessage;
import ink.zrt.utils.ConsumerHandler;
import ink.zrt.utils.WrappedConsumer;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.message.MessageView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HistoryDataConsumer implements ConsumerHandler {
    private WrappedConsumer wrappedConsumer;
    private final HashMap<String, LinkedList<DeviceMessage>> hm = new HashMap<>();
    private final Lock lock = new ReentrantLock();
    private final Gson gson = new Gson();
    private final int N;

    public HistoryDataConsumer(int N) throws ClientException {
        this.wrappedConsumer = new WrappedConsumer("device", "display-service", this);
        this.N = N;
    }

    @Override
    public ConsumeResult handle(MessageView mv) {
        byte[] b = new byte[mv.getBody().limit()];

        mv.getBody().get(b);
        String msg = new String(b);
        DeviceMessage deviceMessage = gson.fromJson(msg, DeviceMessage.class);
        this.handleInternal(deviceMessage);

        return ConsumeResult.SUCCESS;
    }

    private void handleInternal(DeviceMessage deviceMessage) {
        lock.lock();
        if (!hm.containsKey(deviceMessage.id)) {
            hm.put(deviceMessage.id, new LinkedList<>());
        }
        var l = hm.get(deviceMessage.id);
        if (l.size() == N) {
            l.pop();
        }
        l.add(deviceMessage);
        lock.unlock();
    }

    public double[] query(String id) {
        lock.lock();
        if (!hm.containsKey(id)) {
            lock.unlock();
            return null;
        }

        var list = hm.get(id);
        double[] ret = new double[list.size()];
        int i = 0;
        for (DeviceMessage deviceMessage : list) {
            ret[i] = deviceMessage.data;
        }
        lock.unlock();
        return ret;
    }
}

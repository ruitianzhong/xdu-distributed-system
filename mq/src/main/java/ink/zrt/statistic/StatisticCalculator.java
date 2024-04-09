package ink.zrt.statistic;

import com.google.gson.Gson;
import ink.zrt.Message.DeviceMessage;
import ink.zrt.Message.StatisticMessage;
import ink.zrt.utils.ConsumerHandler;
import ink.zrt.utils.WrappedConsumer;
import ink.zrt.utils.WrappedProducer;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.message.MessageView;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StatisticCalculator implements ConsumerHandler {

    private final HashMap<String, LinkedList<DeviceMessage>> map = new HashMap<>();
    private final Lock lock = new ReentrantLock();
    private final Gson gson = new Gson();
    private final int N;
    private final WrappedConsumer wrappedConsumer;
    private final WrappedProducer wrappedProducer;


    public StatisticCalculator(int N) throws ClientException {
        this.N = N;
        wrappedConsumer = new WrappedConsumer("device", "statistic", this);
        wrappedProducer = new WrappedProducer("statistic");
    }


    public void run() throws IOException {
        for (; ; ) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                break;
            }
            lock.lock();
            var keys = map.keySet();
            for (String key : keys) {
                calculate(key, map.get(key));
            }
            lock.unlock();
        }
        wrappedProducer.close();
        wrappedConsumer.close();
    }


    private void calculate(String id, LinkedList<DeviceMessage> list) {
        if (list.isEmpty()) {
            return;
        }

        double max = Double.MIN_VALUE, min = Double.MAX_VALUE;
        double sum = 0.0;
        for (DeviceMessage dm : list) {
            if (dm.data > max) {
                max = dm.data;
            }
            if (dm.data < min) {
                min = dm.data;
            }
            sum += dm.data;
        }
        double average = sum / (double) list.size();
        sum = 0;
        for (DeviceMessage dm : list) {
            sum += (dm.data - average) * (dm.data - average);
        }
        StatisticMessage statisticMessage = new StatisticMessage();
        statisticMessage.average = average;
        statisticMessage.max = max;
        statisticMessage.id = id;
        statisticMessage.variance = sum / (double) list.size();
        statisticMessage.min = min;
        wrappedProducer.sendMessage(gson.toJson(statisticMessage));
        System.out.println(gson.toJson(statisticMessage));
    }


    @Override
    public ConsumeResult handle(MessageView mv) {
        byte[] b = new byte[mv.getBody().limit()];
        mv.getBody().get(b);

        String msg = new String(b);
        DeviceMessage dm = gson.fromJson(msg, DeviceMessage.class);
        handleInternal(dm);

        return ConsumeResult.SUCCESS;
    }

    private void handleInternal(DeviceMessage dm) {
        lock.lock();
        if (!map.containsKey(dm.id)) {
            map.put(dm.id, new LinkedList<>());
        }
        System.out.println(dm.data);
        var l = map.get(dm.id);
        if (l.size() == N) {
            l.pop();
        }
        l.add(dm);
        lock.unlock();
    }
}

package ink.zrt.DataDisplay;

import com.google.gson.Gson;
import ink.zrt.Message.StatisticMessage;
import ink.zrt.utils.ConsumerHandler;
import ink.zrt.utils.WrappedConsumer;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.message.MessageView;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SummaryConsumer implements ConsumerHandler {
    private WrappedConsumer wrappedConsumer;
    private final HashMap<String, StatisticMessage> hm = new HashMap<>();
    private final Lock lock = new ReentrantLock();
    private final Gson gson = new Gson();

    public SummaryConsumer() throws ClientException {
        this.wrappedConsumer = new WrappedConsumer("statistic", "display-service", this);
    }

    @Override
    public ConsumeResult handle(MessageView mv) {
        byte[] b = new byte[mv.getBody().limit()];

        mv.getBody().get(b);
        String msg = new String(b);
        System.out.println("Summary: " + msg);
        StatisticMessage statisticMessage = gson.fromJson(msg, StatisticMessage.class);
        this.handleInternal(statisticMessage);

        return ConsumeResult.SUCCESS;
    }

    private void handleInternal(StatisticMessage statisticMessage) {
        lock.lock();
        hm.put(statisticMessage.id, statisticMessage);
        lock.unlock();
    }

    public StatisticMessage query(String id) {
        lock.lock();
        StatisticMessage statisticMessage = hm.get(id);
        lock.unlock();
        return statisticMessage;
    }
}

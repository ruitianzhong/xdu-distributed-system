package ink.zrt.DataDisplay;

import org.apache.rocketmq.client.apis.ClientException;

public class Testing {
    public static void main(String[] args) throws ClientException, InterruptedException {
        SummaryConsumer summaryConsumer = new SummaryConsumer();
        HistoryDataConsumer historyDataConsumer = new HistoryDataConsumer(150);
        Thread.sleep(Integer.MAX_VALUE);
    }
}

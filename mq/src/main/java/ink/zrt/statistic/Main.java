package ink.zrt.statistic;

import org.apache.rocketmq.client.apis.ClientException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws ClientException, IOException {
        StatisticCalculator sc = new StatisticCalculator(200);
        sc.run();
    }
}

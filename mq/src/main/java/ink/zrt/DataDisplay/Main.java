package ink.zrt.DataDisplay;


import org.apache.rocketmq.client.apis.ClientException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) throws ClientException {
        SummaryConsumer sc = new SummaryConsumer();
        HistoryDataConsumer hdc = new HistoryDataConsumer(200);
        DataViewController.summaryConsumer = sc;
        DataViewController.historyDataConsumer = hdc;
        SpringApplication.run(Main.class, args);
    }
}

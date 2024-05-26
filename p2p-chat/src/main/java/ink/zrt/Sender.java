package ink.zrt;


import com.google.gson.Gson;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class Sender {

    public Sender(int port) {
        this.port = port;
        this.list = new LinkedList<>();
    }

    private final int port;
    LinkedList<Message> list;
    ReentrantLock lock = new ReentrantLock();

    Gson gson = new Gson();

    public void send(Message message) {
        lock.lock();
        list.push(message);
        lock.unlock();
    }

    public void start() {
        new Thread(() -> {
            while (true) {
                lock.lock();
                if (list.isEmpty()) {
                    lock.unlock();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    continue;
                }
                Message m = list.pop();
                lock.unlock();
                sendMessage(m);
            }

        }).start();
    }

    private void sendMessage(Message message) {
        String url = "http://localhost:" + port;
        while (true) {
            try {
                HttpClient httpClient = HttpClientBuilder.create().build();

                HttpPost httpPost = new HttpPost(url);
                RequestConfig requestConfig = RequestConfig.custom().build();
                httpPost.setConfig(requestConfig);
                httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
                httpPost.setEntity(new StringEntity(gson.toJson(message), StandardCharsets.UTF_8));
                System.out.println(gson.toJson(message));
                HttpResponse response = httpClient.execute(httpPost);

                if (response.getCode() == 200) {
                    System.out.println("received successfully");
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

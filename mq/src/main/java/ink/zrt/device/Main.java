package ink.zrt.device;

import org.apache.rocketmq.client.apis.ClientException;

import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        Thread[] t = new Thread[2];
        for (int i = 0; i < 2; i++) {
            int finalI = i;
            t[i] = new Thread(() -> {
                try {
                    new Device(finalI + 1).run();
                } catch (IOException | ClientException e) {
                    System.out.println(e.getMessage());
                }
            });
            t[i].start();
        }
        for (int i = 0; i < 2; i++) {
            try {
                t[i].join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

    }

}
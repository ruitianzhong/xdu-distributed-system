package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadTCPServer {
    int port;
    ServerSocket serverSocket;

    MultiThreadTCPServer(int port) {
        this.port = port;
    }

    public void Start() {
        try {
            serverSocket = new ServerSocket(port);
            int count = 0;
            while (true) {
                Socket client = serverSocket.accept();
                new Thread(() -> {
                    VectorCalculation.handleRequest(client);
                }).start();
                System.out.println(++count);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

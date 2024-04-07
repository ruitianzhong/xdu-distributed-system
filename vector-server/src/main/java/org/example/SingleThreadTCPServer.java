package org.example;

import java.io.IOException;
import java.net.ServerSocket;

public class SingleThreadTCPServer {
    int port;
    ServerSocket serverSocket;

    SingleThreadTCPServer(int port) {
        this.port = port;

    }

    public void Start() {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                var client = serverSocket.accept();
                VectorCalculation.handleRequest(client);
            }

        } catch (IOException e) {

            e.printStackTrace();
            System.out.println("Server Error");
        }


    }


}

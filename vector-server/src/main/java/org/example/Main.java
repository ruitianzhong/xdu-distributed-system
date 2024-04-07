package org.example;

public class Main {
    public static void main(String[] args) {
        var s1 = new MultiThreadTCPServer(1100);
        s1.Start();
    }
}
package org.example;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    public static void main(String[] args) {
        try {
            Socket client = new Socket("127.0.0.1", 1100);

            for (int i = 0; i < 6; i++) {
                String sb = String.valueOf((char) ('0' + i + 1)) +
                        ' ';
                client.getOutputStream().write(sb.getBytes(StandardCharsets.UTF_8));
            }
            client.getOutputStream().flush();
            client.shutdownOutput();

            InputStream is = client.getInputStream();
            StringBuilder output = new StringBuilder();
            while (true) {
                byte[] b = new byte[1024];

                int len = is.read(b);
                if (len > 0) {
                    String s = new String(b, 0, len);
                    output.append(s);
                } else if (len == -1) {
                    System.out.println(output);
                    break;
                }
            }
            client.close();
        } catch (Exception ignored) {

        }

    }
}

package org.example;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class VectorCalculation {

    public static int dotProduct(int[] a, int[] b) throws Exception {
        if (a.length != 3 || b.length != 3) {
            throw new Exception("Bad parameter");
        }
        int result = 0;
        for (int i = 0; i < 3; i++) {
            result += a[i] * b[i];
        }
        return result;
    }

    public static int[] crossProduct(int[] a, int[] b) throws Exception {
        if (a.length != 3 && b.length != 3) {
            throw new Exception("Bad parameter");
        }
        int[] result = new int[3];
        result[0] = a[1] * b[2] - b[1] * a[2];
        result[1] = b[0] * a[2] - b[2] * a[0];
        result[2] = a[0] * b[1] - b[0] * a[1];
        return result;
    }

    public static void main(String[] args) throws Exception {
        int[] a = {1, 2, 3};
        int[] b = {4, 5, 6};
        System.out.println(Arrays.toString(crossProduct(a, b)));
    }

    public static void handleRequest(Socket client) {

        try {
            handleRequestInternal(client.getOutputStream(), client.getInputStream());
            client.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private static void handleRequestInternal(OutputStream os, InputStream is) throws Exception {

        Scanner s = new Scanner(is);


        int[] a = new int[3], b = new int[3];
        int i = 0;
        for (; i < 6 && s.hasNextInt(); i++) {
            int t = s.nextInt();
            if (i < 3) {
                a[i] = t;
            } else {
                b[i - 3] = t;
            }
        }


        if (i != 6) {
            return;
        }

        os.write(String.valueOf(dotProduct(a, b)).getBytes(StandardCharsets.UTF_8));
        os.write("\r\n".getBytes(StandardCharsets.UTF_8));
        os.write(Arrays.toString(crossProduct(a, b)).getBytes(StandardCharsets.UTF_8));

    }
}

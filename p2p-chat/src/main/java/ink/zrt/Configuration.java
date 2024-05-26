package ink.zrt;

import ink.zrt.chatclient.Client;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class Configuration {

    public static int[] ports = {8080, 8081, 8082};
    private static final HashSet<String> msg = new HashSet<>();
    private static final ReentrantLock lock = new ReentrantLock();


    public static Node node = new Node();

    public static void init(int port) {
        JTextArea showArea = new JTextArea(25, 34);

        Configuration.node.init(port, Configuration.ports, showArea);
        Client client = new Client(showArea, Configuration.node);
        SwingUtilities.invokeLater(client::createAndShowGUI);

        String[] portArg = new String[]{"--server.port=" + port};
        SpringApplication.run(Main.class, portArg);

    }

    public static boolean setIfExist(String key) {
        boolean exist;
        lock.lock();
        exist = msg.contains(key);
        if (!exist) {
            msg.add(key);
        }
        lock.unlock();
        return exist;
    }

}

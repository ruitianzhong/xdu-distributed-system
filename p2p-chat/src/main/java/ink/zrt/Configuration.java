package ink.zrt;

import ink.zrt.chatclient.Client;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class Configuration {

    public static int[] ports = {8080, 8081, 8082};

    public static Node node = new Node();

    public static void init(int port) {
        JTextArea showArea = new JTextArea(25, 34);

        Configuration.node.init(port, Configuration.ports, showArea);
        Client client = new Client(showArea, Configuration.node);
        SwingUtilities.invokeLater(client::createAndShowGUI);

        String[] portArg = new String[]{"--server.port=" + port};
        SpringApplication.run(Main.class, portArg);

    }
}

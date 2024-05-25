package ink.zrt;

import ink.zrt.chatclient.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        JTextArea showArea = new JTextArea(25, 34);

        Configuration.node.init(8080, new int[]{8080}, showArea);
        Client client = new Client(showArea, Configuration.node);
        SwingUtilities.invokeLater(client::createAndShowGUI);

        SpringApplication.run(Main.class, args);
    }
}
package ink.zrt.chatclient;

import ink.zrt.Message;
import ink.zrt.Node;

import java.awt.*;

import javax.swing.*;

public class Client {

    JTextArea showArea;

    Node node;

    public Client(JTextArea showArea, Node node) {
        this.showArea = showArea;
        this.node = node;
    }

    public Client() {
    }

    public void createAndShowGUI() {
        JFrame f = new JFrame("P2P 群聊");
        f.setLayout(new BorderLayout());
        f.setSize(500, 500);
        f.setLocation(300, 200);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JScrollPane scrollPane = new JScrollPane(showArea);
        showArea.setEditable(false);
        JPanel panel = getPanel(showArea);
        f.add(scrollPane, "North");
        f.add(panel, "West");
    }

    private JPanel getPanel(JTextArea showArea) {
        JTextField inputField = new JTextField(20);
        JButton btn = new JButton("发送");
        btn.addActionListener(e -> {
            String content = inputField.getText();

            if (content != null && !content.trim().isEmpty()) {
                Message message = new Message("-1", -1, content, node.getNodeId(), false);
                node.broadcastMessage(message);
            } else {
                showArea.append("聊天消息不为空" + "\n");
            }
            inputField.setText("");
        });
        JPanel panel = new JPanel();
        JLabel label = new JLabel("请输入需要发送的消息");
        panel.add(label);
        panel.add(inputField);
        panel.add(btn);
        return panel;
    }

    public void start() {
        SwingUtilities.invokeLater(this::createAndShowGUI);
    }

    public static void main(String[] args) {
        new Client().start();

    }
}

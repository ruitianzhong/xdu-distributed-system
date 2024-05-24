package ink.zrt.chatclient;

import java.awt.*;

import javax.swing.*;

public class Client {

    public int id;
    JTextArea showArea;

    public Client(JTextArea showArea) {
        this.showArea = showArea;
    }

    public Client() {
    }

    public void createAndShowGUI() {
        //1.创建一个JFrame聊天窗口
        JFrame f = new JFrame("P2P 群聊");
        f.setLayout(new BorderLayout());
        f.setSize(500, 500);
        f.setLocation(300, 200);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //2.创建一个JTextArea文本域，用来显示多行聊天信息

        //创建一个JScrollPane滚动面板组件，将JTextArea文本框作为显示组件
        JScrollPane scrollPane = new JScrollPane(showArea);
        showArea.setEditable(false);
        JPanel panel = getPanel(showArea);
        //5.向JFrame聊天窗口的顶部和尾部分别加入面板组件JScrollPane和JPanel
        f.add(scrollPane, "North");
        f.add(panel, "West");
    }

    private static JPanel getPanel(JTextArea showArea) {
        JTextField inputField = new JTextField(20);
        JButton btn = new JButton("发送");
        btn.addActionListener(e -> {
            String content = inputField.getText();

            if (content != null && !content.trim().isEmpty()) {
                //如果不为空，将输入的文本追加到聊天窗口
                showArea.append("发送：" + content + "\n");
            } else {

                showArea.append("聊天消息不为空" + "\n");
            }
            inputField.setText("");//将输入的文本域内容置为空
        });
        //4.创建一个JPanel面板组件
        JPanel panel = new JPanel();
        JLabel label = new JLabel("请输入需要发送的消息");//创建一个标签
        panel.add(label);//将标签组件添加到面板上去
        panel.add(inputField);//将文本组件添加到面板上去
        panel.add(btn);//将按钮添加到面板上去
        return panel;
    }

    public void start() {
        SwingUtilities.invokeLater(this::createAndShowGUI);
    }

    public static void main(String[] args) {
        new Client().start();

    }
}

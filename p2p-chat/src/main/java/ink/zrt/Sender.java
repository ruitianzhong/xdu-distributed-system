package ink.zrt;


import java.util.LinkedList;
import java.util.List;

public class Sender {

    public Sender(int port) {
        this.port = port;
        this.list = new LinkedList<>();
    }

    private int port;
    LinkedList<Message> list;

    synchronized public void send(Message message) {


    }


}

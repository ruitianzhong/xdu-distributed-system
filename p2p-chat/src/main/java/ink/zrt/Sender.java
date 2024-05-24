package ink.zrt;


import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class Sender {

    public Sender(int port) {
        this.port = port;
        this.list = new LinkedList<>();
    }

    private int port;
    LinkedList<Message> list;
    ReentrantLock lock = new ReentrantLock();

    public void send(Message message) {
        lock.lock();
        list.push(message);
        lock.unlock();
    }

    public void start() {

        new Thread(() -> {
            while (true) {
                lock.lock();
                if (list.isEmpty()) {
                    lock.unlock();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    continue;
                }
                Message m = list.pop();
                lock.unlock();
            }

        }).start();
    }


}

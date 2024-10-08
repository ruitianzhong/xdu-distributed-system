package ink.zrt;

import javax.swing.*;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class ReceiveQueue {

    Node node;
    JTextArea area;

    public ReceiveQueue(Node node, JTextArea area) {
        list = new LinkedList<>();
        lock = new ReentrantLock();
        this.node = node;
        this.area = area;
    }

    final private LinkedList<Message> list;
    final private ReentrantLock lock;

    public void add(Message message) {
        lock.lock();
        list.push(message);
        list.sort(ReceiveQueue::compare);
        lock.unlock();
    }

    public Message first() {
        lock.lock();

        Message m = null;
        if (!list.isEmpty()) {
            m = list.getFirst();
        }
        lock.unlock();
        return m;
    }

    public Message pop() {
        lock.lock();
        Message m = null;
        if (!list.isEmpty()) {
            m = list.removeFirst();
        }
        lock.unlock();
        return m;
    }

    public int size() {
        return list.size();
    }

    private static int compare(Message m1, Message m2) {
        // compare timestamp
        if (m1.getTs() < m2.getTs()) {
            return -1;
        } else if (m1.getTs() > m2.getTs()) {
            return 1;
        }
        // compare node id
        if (m1.getNodeID() < m2.getNodeID()) {
            return -1;
        } else if (m1.getNodeID() > m2.getNodeID()) {
            return 1;
        }
        // we need total order
        throw new RuntimeException("configuration error: same node id");
    }

    public void start() {

        new Thread(() -> {
            while (true) {
                lock.lock();
                if (list.isEmpty()) {
                    lock.unlock();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                    continue;
                }
                // check first
                Message m = list.getFirst();
                if (node.nodeNum() == node.ackCount(m.getId())) {
                    node.deleteAckCountRecord(m.getId());
                    list.removeFirst(); // remove the element ( aka, send to the application layer)
                    area.append("Node:" + m.getNodeID() + " : " + m.getMsg() + "\n");
                }
                lock.unlock();
            }
        }).start();
    }
}

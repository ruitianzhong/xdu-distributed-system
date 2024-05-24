package ink.zrt;

import javax.swing.*;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Node {

    public Node() {
    }

    public Sender[] peers;
    public long clock;

    public ReentrantLock lock;

    HashMap<String, Integer> map = new HashMap<>();

    public int nodeId;

    ReceiveQueue queue;

    public void init(int nodeId, int[] ports, JTextArea showArea) {
        this.peers = new Sender[ports.length];
        this.clock = 0;
        lock = new ReentrantLock();
        this.nodeId = nodeId;

        for (int i = 0; i < ports.length; i++) {
            peers[i] = new Sender(ports[i]);

        }
        queue = new ReceiveQueue(this, showArea);
    }

    public void broadcast(Message message) {
        lock.lock();
        message.setTs(clock);
        message.setNodeID(nodeId);
        message.setId(nodeId + ":" + clock);
        // make sure that messages are ordered by time stamp
        for (var peer : peers) {
            peer.send(message);
        }
        // TODO add clock
        clock++;
        lock.unlock();
    }

    public void addMessage(Message msg) {
        lock.lock();
        assert (!msg.isACK());
        queue.add(msg);
        // TODO: adjust clock
        lock.unlock();
    }

    public void addAck(Message msg) {
        // TODO: adjust clock
        assert (msg.isACK());
        lock.lock();
        if (!map.containsKey(msg.getId())) {
            map.put(msg.getId(), 1);
        } else {
            int count = map.get(msg.getId());
            count++;
            map.put(msg.getId(), count);
        }

        lock.unlock();
    }

    public int ackCount(String id) {
        int ret = 0;
        lock.lock();
        if (map.containsKey(id)) {
            ret = map.get(id);
        }
        lock.unlock();
        return ret;
    }

    public int nodeNum() {
        return peers.length;
    }

    public void deleteAckCountRecord(String id) {
        lock.lock();
        map.remove(id);
        lock.unlock();
    }

}

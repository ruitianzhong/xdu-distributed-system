package ink.zrt;

import lombok.Getter;

import javax.swing.*;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Node {

    public Node() {
    }

    public Sender[] peers;
    @Getter
    public long clock;

    public ReentrantLock lock;

    HashMap<String, Integer> map = new HashMap<>();

    @Getter
    public int nodeId;

    ReceiveQueue queue;

    public void init(int nodeId, int[] ports, JTextArea showArea) {
        this.peers = new Sender[ports.length];
        this.clock = 0;
        lock = new ReentrantLock();
        this.nodeId = nodeId;

        for (int i = 0; i < ports.length; i++) {
            peers[i] = new Sender(ports[i]);
            // starting the sender thread
            peers[i].start();

        }
        queue = new ReceiveQueue(this, showArea);
        // start the reception of the message
        queue.start();

    }

    private void broadcastAck(Message message) {
        lock.lock();
        message.setTs(clock);
        message.setNodeID(nodeId);
        // make sure that messages are ordered by time stamp
        for (var peer : peers) {
            peer.send(message);
        }
        // TODO add clock
        clock++;
        lock.unlock();
    }

    public void broadcastMessage(Message message) {
        lock.lock();
        message.setTs(clock);
        message.setNodeID(nodeId);
        message.setId(this.getNodeId() + ":" + clock);
        // make sure that messages are ordered by time stamp
        for (var peer : peers) {
            peer.send(message);
        }
        clock++;
        lock.unlock();
    }

    private long nextId(long localTs, long remoteTs) {
        return Math.max(localTs, remoteTs) + 1;
    }

    public void addMessage(Message msg) {
        lock.lock();
        assert (!msg.isAck());
        queue.add(msg);
        // it's safe because of reentrant lock.
        clock = nextId(clock, msg.getTs());
        Message ack = new Message(msg.getId(), clock, "This is a ack", this.nodeId, true);
        broadcastAck(ack);// will not block here
        clock++;
        // TODO: adjust clock
        lock.unlock();
    }

    public void addAck(Message msg) {
        assert (msg.isAck());
        lock.lock();
        if (!map.containsKey(msg.getId())) {
            map.put(msg.getId(), 1);
        } else {
            int count = map.get(msg.getId());
            count++;
            map.put(msg.getId(), count);
        }
        clock = nextId(clock, msg.getTs());
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

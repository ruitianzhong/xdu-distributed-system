package ink.zrt;

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

    ReceiveQueue queue = new ReceiveQueue();

    public void init(int nodeId, int[] ports) {
        this.peers = new Sender[ports.length];
        this.clock = 0;
        lock = new ReentrantLock();
        this.nodeId = nodeId;

        for (int i = 0; i < ports.length; i++) {
            peers[i] = new Sender(ports[i]);
        }
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
        lock.unlock();
    }

    public void addMessage(Message msg) {
        lock.lock();
        assert (!msg.isACK());
        queue.add(msg);
        lock.unlock();
    }

    public void addAck(Message msg) {
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
}

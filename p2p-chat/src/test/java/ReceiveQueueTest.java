import ink.zrt.Message;
import ink.zrt.ReceiveQueue;
import org.junit.Assert;
import org.junit.Test;

public class ReceiveQueueTest {
    @Test
    public void TestOrderTS() {
        Message m1 = new Message(), m2 = new Message();
        m1.setTs(1);
        m2.setTs(2);

        ReceiveQueue receiveQueue = new ReceiveQueue();
        receiveQueue.add(m2);
        receiveQueue.add(m1);

        Assert.assertEquals(receiveQueue.pop().getTs(), 1);
        Assert.assertEquals(receiveQueue.pop().getTs(), 2);
    }

    @Test
    public void TestOrderNodeId() {
        Message m1 = new Message(), m2 = new Message();
        m1.setTs(1);
        m1.setNodeID(1);
        m2.setTs(1);
        m2.setNodeID(2);
        ReceiveQueue queue = new ReceiveQueue();
        queue.add(m2);
        queue.add(m1);
        Assert.assertEquals(queue.pop().getNodeID(), 1);
        Assert.assertEquals(queue.pop().getNodeID(), 2);

    }
}

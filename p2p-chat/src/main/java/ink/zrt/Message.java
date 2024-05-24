package ink.zrt;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Message {
    @NotNull(message = "id 不能为空")
    private String id;
    private long ts;
    private String msg;
    private int nodeID;

    private boolean isACK;

    public Message() {
    }

    public Message(String id, long ts, String msg, int nodeID) {
        this.id = id;
        this.ts = ts;
        this.msg = msg;
        this.nodeID = nodeID;
    }

    public Message duplicate() {
        return new Message(this.id, this.ts, this.msg, this.nodeID);
    }


}

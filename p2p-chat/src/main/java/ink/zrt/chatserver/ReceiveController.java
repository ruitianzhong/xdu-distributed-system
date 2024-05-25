package ink.zrt.chatserver;

import ink.zrt.Configuration;
import ink.zrt.Message;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReceiveController {
    @PostMapping("/")
    String root(@RequestBody @Valid Message message) {
        System.out.println("Controller msg:" + message);
        if (message.isAck()) {
            Configuration.node.addAck(message);
        } else {
            Configuration.node.addMessage(message);
        }
        return "ok";
    }
}

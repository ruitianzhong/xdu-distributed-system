package ink.zrt.chatserver;

import ink.zrt.Message;
import ink.zrt.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReceiveController {


    @PostMapping("/")
    String root(@RequestBody @Validated Message message) {
        if (message.isACK()) {

        } else {

        }

        return "ok";
    }
}

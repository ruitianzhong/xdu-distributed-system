package ink.zrt.DataDisplay;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SummaryController {
    @RequestMapping("/summary/{id}")
    public String HelloWorld(@PathVariable(value = "id") String id) {
        return id;
    }
}

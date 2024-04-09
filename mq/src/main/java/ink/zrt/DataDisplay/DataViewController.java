package ink.zrt.DataDisplay;

import com.google.gson.Gson;
import ink.zrt.Message.StatisticMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DataViewController {

    public static HistoryDataConsumer historyDataConsumer;
    public static SummaryConsumer summaryConsumer;


    @GetMapping("/view/{id}")
    public String index(Model model, @PathVariable(value = "id") String id) {
        StatisticMessage statisticMessage = summaryConsumer.query(id);
        double[] history = historyDataConsumer.query(id);

        if (history == null || statisticMessage == null) {
            return "404";
        }

        model.addAttribute("y_axis", new Gson().toJson(history));

        model.addAttribute("summary", statisticMessage);
        return "index";
    }
}

package ink.zrt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class Configuration {

    public static int[] ports = {8080, 8081, 8082};

    public static Node node = new Node();
}

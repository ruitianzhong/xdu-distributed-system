package ink.zrt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        // gson example
        Configuration.node.init(8080, new int[]{8080});
        SpringApplication.run(Main.class, args);
    }
}
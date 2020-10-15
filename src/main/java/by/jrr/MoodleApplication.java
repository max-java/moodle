package by.jrr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class MoodleApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(MoodleApplication.class, args);
    }
}

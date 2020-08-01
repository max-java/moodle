package by.jrr;

import by.jrr.library.service.IsbnBookSearchService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class MoodleApplication {

    public static final String isbn = "9781617294945";

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(MoodleApplication.class, args);
//        IsbnBookSearchService service = new IsbnBookSearchService();
//        service.findByIsbn("9781617294945");
    }
}



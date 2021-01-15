package by.jrr;

import by.jrr.profile.service.TimeLineNotificationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class MoodleApplication {

    public static void main(String[] args) {
//        ApiContextInitializer.init();
        ConfigurableApplicationContext ctx = SpringApplication.run(MoodleApplication.class, args);
    }
}

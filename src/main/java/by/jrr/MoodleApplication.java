package by.jrr;

import by.jrr.profile.service.TimeLineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class MoodleApplication {

    public static void main(String[] args) {
//        ApiContextInitializer.init();
        ConfigurableApplicationContext ctx = SpringApplication.run(MoodleApplication.class, args);
        TimeLineService timeLineService = ctx.getBean(TimeLineService.class);
        timeLineService.globalUpdateForUuid();

    }
}

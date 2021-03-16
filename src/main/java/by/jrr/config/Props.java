package by.jrr.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

//todo: use Spring out of the box
@Configuration
@PropertySource("application.properties")
@Getter
public class Props {

    @Value("base.url")
    private String baseUrl;
}

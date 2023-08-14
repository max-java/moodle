package by.jrr;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

//@EnableDiscoveryClient
@SpringBootApplication
//@EnableFeignClients
//@EnableScheduling
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class MoodleApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoodleApplication.class, args);
    }
}

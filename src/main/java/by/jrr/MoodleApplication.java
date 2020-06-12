package by.jrr;

import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.service.UserService;
import by.jrr.interview.parser.QAndAParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SpringBootApplication
public class MoodleApplication {

	public static void main(String[] args) {
//		ConfigurableApplicationContext ctx =
				SpringApplication.run(MoodleApplication.class, args);
//		QAndAParser qAndAParser = (QAndAParser) ctx.getBean("QAndAParser");
//		qAndAParser.run();
	}
}


package by.jrr;

import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.service.UserService;
import by.jrr.interview.parser.QAndAParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MoodleApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(MoodleApplication.class, args);
		QAndAParser qAndAParser = (QAndAParser) ctx.getBean("QAndAParser");
		qAndAParser.run();
	}
}

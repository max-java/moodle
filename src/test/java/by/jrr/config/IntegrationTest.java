package by.jrr.config;

import by.jrr.MoodleApplication;
import by.jrr.balance.currency.bean.BynUsdRate;
import by.jrr.balance.currency.repository.BynUsdRateRepository;
import by.jrr.config.TestProfileConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.List;


@SpringBootTest(classes = {MoodleApplication.class, TestProfileConfig.class})
@ActiveProfiles("test")
//@DataJpaTest
public class IntegrationTest {

}

package by.jrr.balance.currency;

import by.jrr.config.IntegrationTest;
import by.jrr.balance.currency.bean.BynUsdRate;
import by.jrr.balance.currency.repository.BynUsdRateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import javax.annotation.Resource;
import java.util.List;

@Sql("/balance.sql")
public class TestTest extends IntegrationTest {

//    @Autowired
//    BynUsdRateService bynUsdRateService;
    @Resource
    BynUsdRateRepository bynUsdRateRepository;

    @Test
    public void testInit() {
        bynUsdRateRepository.save(new BynUsdRate());
        List<BynUsdRate> rates = (List) bynUsdRateRepository.findAll();
        System.out.println(">>>>>>>rates.size() = " + rates.size());
    }
}

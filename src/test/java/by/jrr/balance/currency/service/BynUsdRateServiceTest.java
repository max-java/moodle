package by.jrr.balance.currency.service;

import by.jrr.balance.currency.bean.BynUsdRate;
import by.jrr.balance.currency.repository.BynUsdRateRepository;
import by.jrr.config.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;

import javax.annotation.Resource;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Sql("/balance.sql")
class BynUsdRateServiceTest extends IntegrationTest {

    @Resource
    private BynUsdRateRepository bynUsdRateRepository;
    private BynUsdRateService bynUsdRateService;

    @BeforeEach
    public void setup() {
        bynUsdRateService = new BynUsdRateService(bynUsdRateRepository);
    }

    @Test
    void getRateOnDate() {
        BynUsdRate actual = bynUsdRateService.getRateOnDate(LocalDate.parse("2020-12-05"));
        System.out.println("actual = " + actual);
    }
}

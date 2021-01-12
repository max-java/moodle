package by.jrr.balance.currency.repository;

import by.jrr.balance.currency.bean.BynUsdRate;
import by.jrr.config.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Sql("/balance.sql")
class BynUsdRateRepositoryTest extends IntegrationTest {

    @Resource
    BynUsdRateRepository bynUsdRateRepository;

    @Test
    void findByDate() {
        Optional<BynUsdRate> result = bynUsdRateRepository.findByDate(LocalDate.parse("2020-12-05"));
        assertEquals(createExpectedBynUsdRate(), result.get());
    }

    @Test
    void findAll() {
        List<BynUsdRate> rates = (List) bynUsdRateRepository.findAll();
        assertEquals(rates.size(), 1);
    }

    @Test
    void save() {
        BynUsdRate actual = bynUsdRateRepository.save(createNewBynUsdRate());
        BynUsdRate expected = new BynUsdRate(10000L, LocalDate.parse("2020-12-04"), "USD",BigDecimal.valueOf(2.58));
        List<BynUsdRate> rates = (List) bynUsdRateRepository.findAll();
        assertEquals(rates.size(), 2);
        assertEquals(expected, actual);
    }

    public static BynUsdRate createExpectedBynUsdRate() {
        return new BynUsdRate(1L, LocalDate.parse("2020-12-05"), "USD",BigDecimal.valueOf(2.57));
    }

    public static BynUsdRate createNewBynUsdRate() {
        return new BynUsdRate(null, LocalDate.parse("2020-12-04"), "USD",BigDecimal.valueOf(2.58));
    }
}

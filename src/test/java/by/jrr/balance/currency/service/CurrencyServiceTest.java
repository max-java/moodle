package by.jrr.balance.currency.service;

import by.jrr.balance.currency.bean.BynUsdRate;
import by.jrr.balance.currency.common.Mocked;
import by.jrr.balance.currency.repository.BynUsdRateRepository;
import by.jrr.config.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Sql("/balance.sql")
class CurrencyServiceTest extends IntegrationTest {

    @SpyBean
    private BynUsdRateService bynUsdRateService;
    @SpyBean
    private NbrbService nbrbService;

    private CurrencyService currencyService;

    private LocalDate date = LocalDate.parse("2020-12-05");

    @BeforeEach
    public void setup() {
        this.currencyService = new CurrencyService(bynUsdRateService, nbrbService);
    }

    @Test
    public void getBynUsdRateOnDateShouldGetDatabaseValue() {
        BigDecimal result = currencyService.getBynUsdRateOnDate(LocalDate.parse("2020-12-05"));

        verify(bynUsdRateService).getRateOnDate(date);
        verifyNoMoreInteractions(nbrbService, bynUsdRateService);
        assertEquals(BigDecimal.valueOf(2.57), result);
    }

    @Test
    public void getBynUsdRateOnDateShouldGetNbRbValueAndStoreInDatabase() {
        LocalDate myDate = LocalDate.parse("2020-12-04");
        when(nbrbService.getBynToUsdOnDate(myDate)).thenReturn(Mocked.getRateDto());
        BigDecimal result = currencyService.getBynUsdRateOnDate(myDate);

        verify(bynUsdRateService).getRateOnDate(myDate);
        verify(nbrbService).getBynToUsdOnDate(myDate);
        verify(bynUsdRateService).saveRate(Mocked.getRateDto());
        assertEquals(BigDecimal.valueOf(2.5862), result);
    }
}

package by.jrr.balance.currency.mapper;

import by.jrr.balance.currency.bean.BynUsdRate;
import by.jrr.balance.currency.common.Mocked;
import by.jrr.balance.currency.dto.RateDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RateMapperTest {

    @Test
    void getBynUsdFromRateDto() {
        RateDto rateDto = Mocked.getRateDto();
        BynUsdRate expected = Mocked.getBynUsdRate();

        BynUsdRate actual = RateMapper.OF.getBynUsdFromRateDto(rateDto);
        assertEquals(expected, actual);
    }
}

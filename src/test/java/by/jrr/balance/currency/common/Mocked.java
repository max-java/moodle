package by.jrr.balance.currency.common;

import by.jrr.balance.currency.bean.BynUsdRate;
import by.jrr.balance.currency.dto.RateDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Mocked {

    public static RateDto getRateDto() {
        return new RateDto(
                        145,
                        LocalDateTime.parse("2020-12-04T00:00"),
                        "USD",
                        1,
                        "Доллар США",
                        2.5862);
    }

    public static BynUsdRate getBynUsdRate() {
        return new BynUsdRate(
                null,
                LocalDate.parse("2020-12-04"),
                "USD",
                BigDecimal.valueOf(2.5862));
    }
}

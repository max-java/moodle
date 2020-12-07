package by.jrr.balance.currency.proxy;


import by.jrr.balance.currency.dto.RateDto;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;

@FeignClient(name="forex-service", url="https://www.nbrb.by")
public interface NbrbProxy {
    @GetMapping("/api/exrates/rates/{code}?ondate={date}&parammode=2")
    RateDto getBynToCurrencyOnDate(@PathVariable("code") String currencyCode, @PathVariable("date") String date);
}

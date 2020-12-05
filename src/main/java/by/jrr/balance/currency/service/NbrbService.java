package by.jrr.balance.currency.service;

import by.jrr.balance.currency.dto.RateDto;
import by.jrr.balance.currency.proxy.NbrbProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class NbrbService {

    @Autowired
    NbrbProxy nbrbProxy;

    public RateDto getBynToUsdOnDate(LocalDate date) {
        return nbrbProxy.getBynToCurrencyOnDate("USD", date.toString());
    }
}

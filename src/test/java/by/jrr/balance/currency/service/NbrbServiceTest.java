package by.jrr.balance.currency.service;

import by.jrr.balance.currency.dto.RateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class NbrbServiceTest {

    @Autowired
    NbrbService nbrbService;

    @Test
    void getBynToUsdOnDate() {
        //comment this to avoid unnecessary calls
//        RateDto curr = nbrbService.getBynToUsdOnDate(null);
    }
}

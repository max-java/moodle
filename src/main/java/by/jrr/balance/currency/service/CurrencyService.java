package by.jrr.balance.currency.service;

import by.jrr.balance.bean.Currency;
import by.jrr.balance.bean.OperationRow;
import by.jrr.balance.currency.bean.BynUsdRate;
import by.jrr.balance.currency.dto.RateDto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class CurrencyService {

    final private Logger logger = LoggerFactory.getLogger(CurrencyService.class);

    final BynUsdRateService bynUsdRateService;
    final NbrbService nbrbService;

    public CurrencyService(BynUsdRateService bynUsdRateService, NbrbService nbrbService) {
        this.bynUsdRateService = bynUsdRateService;
        this.nbrbService = nbrbService;
    }

    //todo run integration tests and validate currency selects from db.
    public BigDecimal getBynUsdRateOnDate(LocalDate date) {
        LocalDate dateNow = LocalDate.now();
        if(dateNow.isBefore(date)) {
            logger.debug("Date {} is in future. Set to current date {}", date, dateNow);
            date = dateNow;
        }
        BynUsdRate bynUsdRate = bynUsdRateService.getRateOnDate(date);

        if (bynUsdRate.getId() == null) {
//            getFromNbRbAndSaveToDbOrSetToZero(date, bynUsdRate);
            //todo: @max !cito! fix call to NbRb Service.
            bynUsdRate.setSum(BigDecimal.valueOf(2.63));
        }
        return bynUsdRate.getSum();
    }

    private void getFromNbRbAndSaveToDbOrSetToZero(LocalDate date, BynUsdRate bynUsdRate) {
        try {
            logger.debug("Rates on date {} not found in database.", date);
            RateDto rateDto = nbrbService.getBynToUsdOnDate(date);
            bynUsdRate = bynUsdRateService.saveRate(rateDto);
            logger.debug("New rates {} saved in database.", rateDto);
        } catch (Exception ex) {
            //todo: get last known value from db or set default 0
            //todo: send exception and notification, that billing not working.
            bynUsdRate.setSum(BigDecimal.ZERO);
        }
    }

    public BigDecimal getOperationRowSumInBynOnOperationDate(OperationRow operationRow) {
        //todo add more currencies
        switch (operationRow.getCurrency()) {
            case USD:
                return operationRow.getSum().multiply(getBynUsdRateOnDate(operationRow.getDate()));
            case BYN:
            default:
                return operationRow.getSum();
        }
    }

    public BigDecimal convertAndGetOperationRowSumOnOperationDate(OperationRow operationRow, Currency convertTo) {
        switch (operationRow.getCurrency()) {
            case USD:
                switch (convertTo) {
                    case USD:
                        return operationRow.getSum();
                    case BYN:
                        return operationRow.getSum().multiply(getBynUsdRateOnDate(operationRow.getDate()));
                }

            case BYN:
                switch (convertTo) {
                    case BYN:
                        return operationRow.getSum();
                    case USD:
                        return operationRow.getSum().divide(getBynUsdRateOnDate(operationRow.getDate()), 4, RoundingMode.HALF_UP);
                }
            default: // todo consider throw unsupported conversion exception
                return operationRow.getSum();
        }
    }

    public BigDecimal convertOnToday(BigDecimal sum, Currency from, Currency to) {
        switch (from) {
            case USD:
                switch (to) {
                    case USD:
                        return sum;
                    case BYN:
                        return sum.multiply(getBynUsdRateOnDate(LocalDate.now()));
                }

            case BYN:
                switch (to) {
                    case BYN:
                        return sum;
                    case USD:
                        return sum.divide(getBynUsdRateOnDate(LocalDate.now()));
                }
            default: // todo consider throw unsupported conversion exception
                return sum;
        }
    }
}

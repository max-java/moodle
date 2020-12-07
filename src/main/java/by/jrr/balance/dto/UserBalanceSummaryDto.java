package by.jrr.balance.dto;

import by.jrr.balance.bean.Currency;
import by.jrr.balance.bean.SummaryOperations;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UserBalanceSummaryDto {

    private Currency currency;

    List<SummaryOperations> summaryOperations;
    Map<Currency, SummaryOperations> summaryGroupedByCurrencies;

    SummaryOperations summaryInCurrentCurrency;
//    private BigDecimal contract = ZERO;
//    private BigDecimal income = ZERO;
//    private BigDecimal invoice = ZERO;
//
//    private BigDecimal invoiceDebt = ZERO;
//    private BigDecimal contractDebt = ZERO;
}

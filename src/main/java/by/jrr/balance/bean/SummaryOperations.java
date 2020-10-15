package by.jrr.balance.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class SummaryOperations {

    private BigDecimal income;
    private BigDecimal outcome;
    private BigDecimal contract;
    private BigDecimal invoice;
    private BigDecimal planIncome;
    private BigDecimal planOutcome;

    private BigDecimal profit;
    private BigDecimal planProfit;
    private BigDecimal contractProfit;
    private BigDecimal invoiceProfit;

    public SummaryOperations() {
        this.income = BigDecimal.ZERO;
        this.outcome = BigDecimal.ZERO;
        this.contract = BigDecimal.ZERO;
        this.invoice = BigDecimal.ZERO;
        this.planIncome = BigDecimal.ZERO;
        this.planOutcome = BigDecimal.ZERO;
        this.profit = BigDecimal.ZERO;
        this.planProfit = BigDecimal.ZERO;
        this.contractProfit = BigDecimal.ZERO;
        this.invoiceProfit = BigDecimal.ZERO;


    }

    public void calculateProfit() {
        profit = income.subtract(outcome);
        planProfit = profit.add(planIncome.subtract(planOutcome));
        contractProfit = contract.subtract(outcome).subtract(planOutcome);
        invoiceProfit = invoice.subtract(outcome).subtract(planOutcome);

        this.income = this.income.setScale(2, RoundingMode.CEILING);
        this.outcome = this.outcome.setScale(2, RoundingMode.CEILING);
        this.contract = this.contract.setScale(2, RoundingMode.CEILING);
        this.invoice = this.invoice.setScale(2, RoundingMode.CEILING);
        this.planIncome = this.planIncome.setScale(2, RoundingMode.CEILING);
        this.planOutcome = this.planOutcome.setScale(2, RoundingMode.CEILING);
        this.profit = this.profit.setScale(2, RoundingMode.CEILING);
        this.planProfit = this.planProfit.setScale(2, RoundingMode.CEILING);
        this.contractProfit = this.contractProfit.setScale(2, RoundingMode.CEILING);
        this.invoiceProfit = this.invoiceProfit.setScale(2, RoundingMode.CEILING);
    }
}

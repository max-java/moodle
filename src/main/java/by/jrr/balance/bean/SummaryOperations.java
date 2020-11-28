package by.jrr.balance.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.BigDecimal.*;

/**
 * Holds sums for in terms of operation row directions and calculate summaries
 * It doesn't calculate future or debts based on dates - such operations are in OperationRow service, where
 * operation rows selects on current (or other ?) date
 **/
@Data
public class SummaryOperations {

    private BigDecimal income = ZERO;
    private BigDecimal outcome = ZERO;
    private BigDecimal contract = ZERO;
    private BigDecimal invoice = ZERO;
    private BigDecimal planIncome = ZERO;
    private BigDecimal planOutcome = ZERO;

    private BigDecimal profit = ZERO;
    private BigDecimal planProfit = ZERO;
    private BigDecimal contractProfit = ZERO;
    private BigDecimal invoiceProfit = ZERO;

    private BigDecimal invoiceDebt = ZERO;
    private BigDecimal contractDebt = ZERO;





    public SummaryOperations() {
    }

    public void calculateProfit() {
        profit = income.subtract(outcome);
        planProfit = profit.add(planIncome.subtract(planOutcome));
        contractProfit = contract.subtract(outcome).subtract(planOutcome);
        invoiceProfit = invoice.subtract(outcome).subtract(planOutcome);

        setScaleRound2Decimals();

    }

    /**
     * User balance is based on Incomes, Contracts and Invoices
     * Sum of contracts = is a total for all payments user should do, both future and late
     * Sum of invoices = is all payments user should do with a specific date. This class considered, that all invoices
     * dates is before current date (see class notes).
     **/
    public void setUserBalance() {

        invoiceDebt = income.subtract(invoice);
        contractDebt = income.subtract(contract);

    }

    public void setScaleRound2Decimals() {
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

        this.invoiceDebt = this.invoiceDebt.setScale(2, RoundingMode.CEILING);
        this.contractDebt = this.contractDebt.setScale(2, RoundingMode.CEILING);
    }

}

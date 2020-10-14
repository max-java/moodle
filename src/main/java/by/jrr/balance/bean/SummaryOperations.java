package by.jrr.balance.bean;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SummaryOperations {

    BigDecimal income;
    BigDecimal outcome;

    private BigDecimal profit;

    public void calculateProfit() {
        profit = income.subtract(outcome);
    }

    private void setProfit(Double profit) {}
}

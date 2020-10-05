package by.jrr.balance.bean;

import lombok.Data;

@Data
public class SummaryOperations {

    Double income;
    Double outcome;

    private Double profit;

    public void calculateProfit() {
        profit = income - outcome;
    }

    private void setProfit(Double profit) {}
}

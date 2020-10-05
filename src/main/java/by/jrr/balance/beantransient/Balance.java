package by.jrr.balance.beantransient;

import by.jrr.balance.bean.Currency;

/**
 * Created by Shelkovich Maksim on 4.3.18.
 */
public class Balance {
    private Currency currency;
    private double total;

    public String getCurrencyName() {
        try {
            return currency.getName();
        } catch (Exception ex) {
            return "";
        }
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

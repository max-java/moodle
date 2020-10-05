package by.jrr.balance.beantransient;

import by.jrr.balance.bean.Currency;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shelkovich Maksim on 1.3.18.
 */

public class CashFlowWithCurrency {
    private Currency currency;
    private List<CashFlowRow> cashFlowRows = new ArrayList<>();

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

    public List<CashFlowRow> getCashFlowRows() {
        return cashFlowRows;
    }

    public void setCashFlowRows(List<CashFlowRow> cashFlowRows) {
        this.cashFlowRows = cashFlowRows;
    }
}

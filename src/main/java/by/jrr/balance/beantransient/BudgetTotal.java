package by.jrr.balance.beantransient;

import by.jrr.balance.bean.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BudgetTotal {
    private double totalIncomesForMonth;
    private double totalOutcomesForMonth;
    private double totalBudgetForMonth;
    private Currency currency;

    public boolean isTotalOutcomesLessThanTotalIncomes() {
        if (totalOutcomesForMonth <= totalIncomesForMonth) {
            return true;
        } else {
            return false;
        }
    }
    public boolean isTotalBudgetLessThanTotalIncomes() {
        if (totalBudgetForMonth <= totalIncomesForMonth) {
            return true;
        } else {
            return false;
        }
    }
    public double incomesNotInBudget() {
        return totalIncomesForMonth - totalBudgetForMonth;
    }
    public double incomesNotInOutcomes() {
        return totalIncomesForMonth - totalOutcomesForMonth;
    }

}

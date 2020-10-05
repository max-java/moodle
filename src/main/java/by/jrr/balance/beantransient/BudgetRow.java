package by.jrr.balance.beantransient;


import by.jrr.balance.bean.Currency;
import by.jrr.balance.bean.OperationCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Shelkovich Maksim on 13.6.19.
 * <p>
 * This class is need for rendering Budget table on budget page
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
public class BudgetRow {

    private OperationCategory operationCategory;
    private double previousMonthTotal;
    private double selectedMonthTotal;
    private double selectedMonthBudget;
    private double selectedMonthFactOutcomes;
    private double selectedMonthPlanOutcomes;
    private Currency currency;
    private Long operationRowId; // budget is an operation row with operationRowDirection=BUDGET, with could be only one operation. This use to add, delete and update MonthBudgetValue as operationRow

    public boolean isSelectedMonthTotalLessThanBudgetTotal() {
        if (selectedMonthTotal  <= selectedMonthBudget) {
            return true;
        } else {
            return false;
        }
    }

    public double totalOutcomesLeftForThisCategory() {
        return selectedMonthBudget - selectedMonthFactOutcomes;
    }
    public OperationCategory getOperationCategory() {
        return operationCategory;
    }

    public void setOperationCategory(OperationCategory operationCategory) {
        this.operationCategory = operationCategory;
    }

    public double getPreviousMonthTotal() {
        return previousMonthTotal;
    }

    public void setPreviousMonthTotal(double previousMonthTotal) {
        this.previousMonthTotal = previousMonthTotal;
    }

    public double getSelectedMonthTotal() {
        return selectedMonthTotal;
    }

    public void setSelectedMonthTotal(double selectedMonthTotal) {
        this.selectedMonthTotal = selectedMonthTotal;
    }

    public double getSelectedMonthBudget() {
        return selectedMonthBudget;
    }

    public void setSelectedMonthBudget(double selectedMonthBudget) {
        this.selectedMonthBudget = selectedMonthBudget;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

}

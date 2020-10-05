package by.jrr.balance.beantransient;

import by.jrr.balance.bean.OperationRow;

import java.time.LocalDate;

/**
 * Created by Shelkovich Maksim on 1.3.18.
 */
// TODO: 2.3.18 can it be omited?
public class CashFlowRow {
    private Long id;
    private OperationRow operationRow;
    private double total;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public OperationRow getOperationRow() {
        return operationRow;
    }

    public void setOperationRow(OperationRow operationRow) {
        this.operationRow = operationRow;
    }
    public LocalDate getDate() {
        try {
            return operationRow.getDate();
        } catch (Exception ex) {
            return null;
        }
    }
    public double getSum() {
//        try {
//            double var = operationRow.getSum();
////            if (operationRow.getIdCashFlowDirection()== OperationRowDirection.PLAN_OUTCOME || operationRow.getIdCashFlowDirection()==OperationRowDirection.OUTCOME) {
//                var = var * -1;
//            }
//            return var;
//        } catch (Exception ex) {
//            return 0;
//        }
        return 0; //todo return 0 while refactoring
    }
//    public String getCurrencyName() {
//        return operationRow.getCurrencyName();
//    }
//    public String getOperationCategoryName() {
//        return operationRow.getOperationCategoryName();
//    }
    public String getNote() {
        try {
            return operationRow.getNote();
        } catch (Exception ex){
            return "";
        }
    }

}

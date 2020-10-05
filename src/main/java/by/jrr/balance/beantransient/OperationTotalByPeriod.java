package by.jrr.balance.beantransient;

import by.jrr.balance.bean.Currency;
import by.jrr.balance.bean.OperationCategory;
import by.jrr.balance.bean.OperationRow;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Shelkovich Maksim on 6.4.18.
 * This class hold all operations selected by specific parameters and reflected total sum for this operations
 */
public class OperationTotalByPeriod {
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private List<OperationRow> operationRowList;
    private int idCashFlowDirection;
    private long idOperationCategory;
    private OperationCategory operationCategory;
    private Long idCurrency;
    private Currency currency;
    private double total;

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public String getOperationCategoryName() {
        try {
            return this.operationCategory.getName();
        } catch (Exception ex) {
            return "no name";
        }
    }
    public String getCurrencyName() {
        try {
            return this.currency.getName();
        } catch (Exception ex) {
            return "no name";
        }
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public List<OperationRow> getOperationRowList() {
        return operationRowList;
    }

    public void setOperationRowList(List<OperationRow> operationRowList) {
        this.operationRowList = operationRowList;
    }

    public long getIdOperationCategory() {
        return idOperationCategory;
    }

    public void setIdOperationCategory(long idOperationCategory) {
        this.idOperationCategory = idOperationCategory;
    }

    public OperationCategory getOperationCategory() {
        return operationCategory;
    }

    public void setOperationCategory(OperationCategory operationCategory) {
        this.operationCategory = operationCategory;
    }

    public int getIdCashFlowDirection() {
        return idCashFlowDirection;
    }

    public void setIdCashFlowDirection(int idCashFlowDirection) {
        this.idCashFlowDirection = idCashFlowDirection;
    }

    public Long getIdCurrency() {
        return idCurrency;
    }

    public void setIdCurrency(Long idCurrency) {
        this.idCurrency = idCurrency;
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

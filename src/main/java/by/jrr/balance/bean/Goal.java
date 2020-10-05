package by.jrr.balance.bean;

import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDate;

/**
 * Запись о поставленной цели накопления.
 */

@Entity
@ToString
public class Goal {

    @Id
    @GeneratedValue
    private Long id;
    private LocalDate date;
    private double sum;
    private long idOperationCategory;
    @Transient
    private OperationCategory operationCategory;

    private String note;
    private int priority;
    private Long idCurrency;
    @Transient
    private Currency currency;

    private Integer repeatableToken;

    public Goal() {

    }
    public String getCurrencyName() {
        try {
            return this.currency.getName();
        } catch (Exception ex) {
            return "";
        }
    }

    public String getOperationCategoryName() {
        try {
            return this.operationCategory.getName();
        } catch (Exception ex) {
            return "";
        }
    }

    public static int compareByPriority(Goal left, Goal right) {
        return left.getPriority() - right.getPriority();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public Integer getRepeatableToken() {
        return repeatableToken;
    }

    public void setRepeatableToken(Integer repeatableToken) {
        this.repeatableToken = repeatableToken;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}

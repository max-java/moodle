package by.jrr.balance.bean;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Запись о поставленной цели накопления.
 */

@Entity
@ToString
@Data
public class Goal {

    @Id
    @GeneratedValue
    private Long id;
    private LocalDate date;
    private BigDecimal sum;
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

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public void setSum(Double sum) {
        setSum(BigDecimal.valueOf(sum));
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
}

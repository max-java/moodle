package by.jrr.balance.bean;

import by.jrr.balance.constant.OperationRowDirection;
import lombok.Data;

import javax.persistence.*;

/**
 * Статьи доходов, расходов и проч. cash flow directions - один элемент для списка вариантов доходов / расходов.
 * CashFlowDirections -
 *
 *
 */
@Entity
@Data
public class OperationCategory {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private int idCashFlowDirection;
    @Transient
    private OperationRowDirection operationRowDirection;

    public OperationCategory(String name, int idCashFlowDirection) {
        this.name = name;
        this.idCashFlowDirection = idCashFlowDirection;
    }

}

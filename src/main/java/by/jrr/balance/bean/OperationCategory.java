package by.jrr.balance.bean;

import by.jrr.balance.constant.OperationRowDirection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Статьи доходов, расходов и проч. cash flow directions - один элемент для списка вариантов доходов / расходов.
 * CashFlowDirections -
 *
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationCategory {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name = "";
    @Enumerated(value = EnumType.STRING)
    private OperationRowDirection operationRowDirection;
}

package by.jrr.balance.bean;

import by.jrr.balance.constant.OperationRowDirection;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Запись о финансовой операции: поступившем доходе - расходе, запланированных операциях
 */

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationRow {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private LocalDate date;
    private Double sum;
    @Enumerated(value = EnumType.STRING)
    private Currency currency;
    @Enumerated(value = EnumType.STRING)
    private OperationRowDirection operationRowDirection;
    private String note;

    private Integer repeatableToken;
}

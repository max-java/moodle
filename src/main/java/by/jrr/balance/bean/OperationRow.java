package by.jrr.balance.bean;

import by.jrr.balance.constant.OperationRowDirection;
import by.jrr.profile.bean.Profile;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
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
    private BigDecimal sum;
    @Enumerated(value = EnumType.STRING)
    private Currency currency;
    @Enumerated(value = EnumType.STRING)
    private OperationRowDirection operationRowDirection;
    private Long idOperationCategory;
    private String note;

    private Integer repeatableToken;

    public void setSum(Double d) {
        setSum(BigDecimal.valueOf(d));
    }

    public void setSum(BigDecimal d) {
        sum = d;
    }

    public BigDecimal getSumInByn() { //todo set USD sum based on date exchange currency!
        switch (currency) {
            case USD: return sum.multiply(BigDecimal.valueOf(2.6));
            default: return sum;
        }
    }

    // TODO: 12/10/2020 consider to move this to dto
    @Transient
    Profile subscriber = new Profile();
    @Transient
    Contract contract = new Contract();
    @Transient
    OperationCategory operationCategory = new OperationCategory();
}

package by.jrr.balance.bean;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcceptanceAct {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    LocalDate date;
    String number;
    BigDecimal sum;
    Currency currency;
    Long contractId;

    public boolean isNew() {
        return id ==null;
    }

    public boolean isEmpty() {
        return id == null && date == null && number == null;
    }
}

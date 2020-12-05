package by.jrr.balance.currency.bean;

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
public class BynUsdRate {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    LocalDate date;
    String currAbbr;
    BigDecimal sum;
}

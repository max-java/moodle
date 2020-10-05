package by.jrr.balance.acontract.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contract {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    LocalDate date;
    String number;

    String notes;

}

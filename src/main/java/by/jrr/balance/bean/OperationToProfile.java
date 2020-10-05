package by.jrr.balance.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationToProfile {


    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    Long operationRowId;
    Long subscriberId;
    Long streamId;

}

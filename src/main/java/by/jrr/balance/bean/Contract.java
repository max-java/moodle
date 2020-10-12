package by.jrr.balance.bean;

import by.jrr.profile.bean.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sun.plugin.util.UserProfile;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contract {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    LocalDate date;
    String number;
    BigDecimal sum;
    Currency currency;


    // TODO: 11/10/2020 ideally this should be moved to DtO
    Long contractTypeId;
    @Transient
    ContractType contractType;
    @Transient
    Profile userProfile = new Profile();
    @Transient
    AcceptanceAct acceptanceAct = new AcceptanceAct(); // TODO: 12/10/2020 consider to remove this costyl. Need to create popUpFormContractWithButton
    @Transient
    List<OperationRow> incomes = new ArrayList<>();

    public String getName() {
        return String.format("от %s № %s на%s%s %s %s ", date, number, sum, currency, contractType.getName(), userProfile.getFullName()); // TODO: 12/10/2020 consider make it good format
    }

}
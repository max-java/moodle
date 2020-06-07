package by.jrr.profile.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreamAndTeamSubscriber {


    @javax.persistence.Id
    @GeneratedValue
    private Long Id;

    private Long streamTeamProfileId;
    private Long subscriberProfileId;
    @Enumerated(value = EnumType.STRING)
    private SubscriptionStatus status;

    @Transient
    private Profile subscriberProfile;

}

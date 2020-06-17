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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)

    private Long Id;

    private Long streamTeamProfileId;
    private Long subscriberProfileId;
    @Enumerated(value = EnumType.STRING)
    private SubscriptionStatus status;

    @Transient
    private Profile subscriberProfile;

    public String getFullSubscriberName() {
        try {
            return this.getSubscriberProfile().getUser().getFullUserName();
        } catch (Exception ex) {
            // TODO: 17/06/20 log exception with details!!
            return "";
        }
    }
}

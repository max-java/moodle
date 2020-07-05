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
    @Transient
    private Profile subscriptionProfile;

    public String getFullSubscriberName() {
        try {
            return this.getSubscriberProfile().getUser().getFullUserName();
        } catch (Exception ex) {
            // TODO: 17/06/20 log exception with details!!
            return "";
        }
    }
    public String getFullSubscriptionName() {
        try {
            return this.getSubscriptionProfile().getUser().getFullUserName();
        } catch (Exception ex) {
            // TODO: 17/06/20 log exception with details!!
            return "";
        }
    }
    public String getSubscriberLogin() {
        try {
            return this.getSubscriberProfile().getUser().getUserName();
        } catch (Exception ex) {
            // TODO: 24/06/20 log exception with details!!
            return "";
        }
    }
    public String getSubscriberEmail() {
        try {
            return this.getSubscriberProfile().getUser().getEmail();
        } catch (Exception ex) {
            // TODO: 24/06/20 log exception with details!!
            return "";
        }
    }
    public String getSubscriberPhone() {
        try {
            return this.getSubscriberProfile().getUser().getPhone();
        } catch (Exception ex) {
            // TODO: 24/06/20 log exception with details!!
            return "";
        }
    }
    public String getSubscriberGitHubUsername() {
        try {
            return this.getSubscriberProfile().getGitUsername();
        } catch (Exception ex) {
            // TODO: 24/06/20 log exception with details!!
            return "";
        }
    }
}

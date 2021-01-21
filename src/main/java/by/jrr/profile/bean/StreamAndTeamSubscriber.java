package by.jrr.profile.bean;

import by.jrr.crm.bean.History;
import by.jrr.crm.bean.Task;
import by.jrr.registration.bean.EventType;
import by.jrr.registration.bean.StudentActionToLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Deprecated //to be deprecated. Need to be renamed to Subscriptions. only db related fields should left, All other fields should be removed from this entity
/***
 * subscriptions is a many to many relation of one profile Id to another. Left should be userId, right - stream of team Id;
 * status - is a enum status.
 */
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

    @Transient // TODO: 08/10/2020 create DTO class for controller
    private List<Task> activeTasks = new ArrayList<>();
    @Transient // TODO: 08/10/2020 create DTO class for controller
    private List<StudentActionToLog> studentActivity = new ArrayList<>();

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

    public boolean hasBreachedActiveTasks() {
        return activeTasks.stream()
                .filter(Task::isActiveBreached)
                .collect(Collectors.toList()).size() > 0;
    }
    public boolean hasActiveTasks() {
        return activeTasks.stream()
                .filter(task -> !task.getIsFinished())
                .collect(Collectors.toList()).size() > 0;
    }

    public String getCardColor() { // TODO: 08/10/2020 it used to colorize card in stream subscribers based on task status. consider more elegance way
        if (hasActiveTasks() && !hasBreachedActiveTasks()) {
            return "card-success";
        }
        if (hasBreachedActiveTasks()) {
            return "card-danger";
        }
        return "";
    }

    public boolean isApproved() {
        return status.equals(SubscriptionStatus.APPROVED);
    }

    public int totalLecturesLogged() { // TODO: 08/10/2020 bind this with timeline item by guid or urlToRedirect if guid is absent
        return studentActivity.stream()
                .filter(l -> l.getEventType().equals(EventType.LECTURE))
                .map(l -> l.getUrlToRedirect())
                .distinct()
                .collect(Collectors.toList()).size();
    }
}

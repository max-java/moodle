package by.jrr.profile.bean;

import by.jrr.auth.bean.User;
import by.jrr.balance.dto.UserBalanceSummaryDto;
import by.jrr.constant.Endpoint;
import by.jrr.moodle.bean.Course;
import by.jrr.telegram.model.TgUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static by.jrr.constant.LinkGenerator.DEFAULT_USERPIC;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Profile {

    // TODO: 10/06/20 Несмотря на то, что профиль пользователя, команда и стрим используют одну и туже вьюху,
    // TODO: 10/06/20 для работы с ними нужно три раздельные сущности


    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;
    @Transient
    private User user;
    private long userId;

    private String avatarFileName;
    private Long ownerProfileId;

    private String telegramLink;
    private String telegramLinkText;
    private String telegramChatToken;

    private String zoomLink;
    private String zoomLinkText;

    private String gitLink;
    private String gitUsername;

    private String feedbackLink;
    private String feedbackName;

    private String userName;
    private String userMiddleName;
    private String userLastName;
    public String getFullName() {return String.format("%s %s %s", userLastName, userName, userMiddleName);}

    @Transient
    public UserBalanceSummaryDto userBalanceSummaryDto;

    @Lob
    private String about;

    @Transient
    private List<StreamAndTeamSubscriber> subscribers = new ArrayList<>();

    public List<StreamAndTeamSubscriber> getSubscribersApproved() {
        return this.subscribers.stream()
                .filter(s -> s.getStatus().equals(SubscriptionStatus.APPROVED))
                .collect(Collectors.toList());
    }

    public List<StreamAndTeamSubscriber> getSubscribersRequested() {
        return this.subscribers.stream()
                .filter(s -> s.getStatus().equals(SubscriptionStatus.REQUESTED))
                .collect(Collectors.toList());
    }

    public List<StreamAndTeamSubscriber> getSubscribersRejected() {
        return this.subscribers.stream()
                .filter(s -> s.getStatus().equals(SubscriptionStatus.REJECTED))
                .collect(Collectors.toList());
    }

    public List<StreamAndTeamSubscriber> getSubscribersCanceled() {
        return this.subscribers.stream()
                .filter(s -> s.getStatus().equals(SubscriptionStatus.CANCELED))
                .collect(Collectors.toList());
    }

    @Transient
    private List<StreamAndTeamSubscriber> subscriptions = new ArrayList<>();

    @Transient
    private ChatButtonDto streamChatButtonDto;
    @Transient
    private List<ChatButtonDto> userChatButtons = new ArrayList<>();

    // that is only for streams
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private Boolean openForEnroll;
    private Boolean free;
    private Long courseId;
    @Transient
    private Course course;
    @Transient
    private TgUser tgUser;

    public boolean isLinkPresent(String link) { // TODO: 05/07/20 this need to make UI button (in ex. zoomLink) visible if link is present
        if (link == null) {
            return false;
        } else if(link.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Profile{toString() not implemented}"; // TODO: 27/07/20 insert this because of stack overflow exception
    }
}

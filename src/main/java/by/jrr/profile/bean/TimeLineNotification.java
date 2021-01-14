package by.jrr.profile.bean;

import by.jrr.registration.bean.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeLineNotification {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;

    private String timelineUUID;  //todo consider and test nullable false updatable false.
    private Long studentProfileId;
    private String lastErrorMessage;

    @Transient
    private Profile studentProfile;
    @Transient
    private TimeLine timeLineEvent;

    @Enumerated(EnumType.STRING)
    private Notification.Type notificationType;
    @Enumerated(EnumType.STRING)
    private Notification.Status notificationStatus;
}

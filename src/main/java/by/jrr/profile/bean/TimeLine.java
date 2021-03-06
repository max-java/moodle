package by.jrr.profile.bean;

import by.jrr.registration.bean.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.glassfish.grizzly.http.util.TimeStamp;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TimeLine {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;
    //todo consider and test nullable false updatable false.
    private String timelineUUID;
    private LocalDateTime dateTime;
    private Long streamTeamProfileId;
    private Long courseId;
    private Long lectureId;
    private String urlToRedirect;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    private String eventName;
    @Column(columnDefinition = "TEXT")
    private String notes;
    private Instant timeStamp;

    @Transient //todo: store in db and update periodically or forceUpdate
    private int totalUniqVisitorsForTimelineEventAroundTimestamp;

    @PrePersist
    protected void onCreate() {
        this.timeStamp = Instant.now();
    }

    public String getTime() {
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}

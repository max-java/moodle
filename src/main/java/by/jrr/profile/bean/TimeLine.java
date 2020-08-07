package by.jrr.profile.bean;

import by.jrr.registration.bean.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeLine {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;
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
}

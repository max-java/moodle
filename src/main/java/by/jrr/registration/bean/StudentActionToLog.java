package by.jrr.registration.bean;

import by.jrr.moodle.bean.Course;
import by.jrr.profile.bean.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentActionToLog {


    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;
    private String timelineUUID;

    private Long studentProfileId;
    @Transient
    private Profile studentProfile;

    private Long streamTeamProfileId;
    @Transient
    private Profile streamTeamProfile;

    private Long courseId;
    @Transient
    private Course course;

    private Long lectureId;
    @Transient
    private Course lecture;

    private String urlToRedirect;
    @CreationTimestamp
    private LocalDateTime timestamp;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    private String eventName;

}

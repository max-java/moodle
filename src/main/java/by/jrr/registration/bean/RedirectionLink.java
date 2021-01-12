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
import java.util.UUID;

/** this is to create link for user with redirection to external resources (zoom, chat)
 * without need to be login, but keeping possible to track with StudentActionToLog (student activity)
 *
 */

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedirectionLink {

    @javax.persistence.Id
    private String uuid;

    private Long studentProfileId;
    private Long streamTeamProfileId;
    private Long courseId;
    private Long lectureId;
    private String urlToRedirect;
    private String redirectionPage;
    private String eventName;
    private int expirationMinutes;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Enumerated(EnumType.STRING)
    private RedirectionLinkStatus status;

    @CreationTimestamp
    private LocalDateTime timestamp;
}

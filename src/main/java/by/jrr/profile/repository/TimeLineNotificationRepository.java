package by.jrr.profile.repository;

import by.jrr.profile.bean.Notification;
import by.jrr.profile.bean.TimeLine;
import by.jrr.profile.bean.TimeLineNotification;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TimeLineNotificationRepository extends PagingAndSortingRepository<TimeLineNotification, Long> {

    Optional<TimeLineNotification> findFirstByTimelineUUIDAndStudentProfileIdAndNotificationType(String timelineUUID,
                                                                                                 Long studentProfileId,
                                                                                                 Notification.Type notificationType);
    Optional<TimeLineNotification> findFirstByNotificationStatusAndNotificationType(Notification.Status notificationStatus,
                                                                                    Notification.Type notificationType);
}

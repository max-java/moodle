package by.jrr.profile.service;

import by.jrr.common.annotations.VisibleForTesting;
import by.jrr.email.service.EMailService;
import by.jrr.profile.bean.Notification;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.bean.TimeLine;
import by.jrr.profile.bean.TimeLineNotification;
import by.jrr.profile.repository.TimeLineNotificationRepository;
import by.jrr.registration.bean.EventType;
import by.jrr.registration.model.RedirectionLinkDto;
import by.jrr.registration.service.RedirectionLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimeLineNotificationService {

    @Autowired
    TimeLineService timeLineService;
    @Autowired
    ProfileService profileService;
    @Autowired
    TimeLineNotificationRepository timeLineNotificationRepository;
    @Autowired
    EMailService eMailService;
    @Autowired
    RedirectionLinkService redirectionLinkService;

    @Scheduled(cron = "0 0 9,15 ? * * ")
    public void createAndSendReminders() {
        System.out.println("creating And Sending Reminders");
        createNotificationsFor(Notification.Type.TODAY_EVENT);
        createNotificationsFor(Notification.Type.TOMORROW_EVENT);

        sendNotificationsFor(Notification.Type.TODAY_EVENT);
        sendNotificationsFor(Notification.Type.TOMORROW_EVENT);
    }


    @Scheduled(cron = "0 */30 * ? * *")
    public void createAndSendRedirectionLinks() {
        System.out.println("creating And Sending Redirection links");
        createNotificationsFor(Notification.Type.REDIRECTION_LINK);

        sendNotificationsFor(Notification.Type.REDIRECTION_LINK);
    }

    public void sendNotificationsFor(Notification.Type type) {
        //todo: create locks for multithreading
        while (nextNotificationIsPresentFor(type)) {
            TimeLineNotification timeLineNotification = getNextNotification(type);
            try {
                sendNotificationReminder(timeLineNotification);
                timeLineNotification.setNotificationStatus(Notification.Status.SENT);
                timeLineNotificationRepository.save(timeLineNotification);
            } catch (Exception ex) {
                timeLineNotification.setNotificationStatus(Notification.Status.ERROR);
                timeLineNotification.setLastErrorMessage(ex.getMessage());
                timeLineNotificationRepository.save(timeLineNotification);
                ex.printStackTrace();
            }
        }
    }

    private void sendNotificationReminder(TimeLineNotification timeLineNotification) {
        TimeLine event = timeLineNotification.getTimeLineEvent();
        setDefaultTextIfValueNotPresent(event);

        Profile student = timeLineNotification.getStudentProfile();

        String email = student.getUser().getEmail();
        String subject = timeLineNotification.getNotificationType().getSubjectText();
        String messageText = makeMessageForNotificationReminder(event, student, timeLineNotification.getNotificationType());

        System.out.println("sending to " + email);
        eMailService.sendEmail(email, subject, messageText);
    }

    private void setDefaultTextIfValueNotPresent(TimeLine event) {
        if (valueNotPresent(event.getNotes())) {
            event.setNotes("отсутствует");
        }
        if (valueNotPresent(event.getEventName())) {
            event.setEventName("отсутствует");
        }
        if (valueNotPresent(event.getNotes())) {
            event.setNotes("отсутствует");
        }
    }

    private boolean valueNotPresent(String value) {
        return value == null || value.equals("null") || value.isEmpty();
    }

    private boolean nextNotificationIsPresentFor(Notification.Type type) {
        return timeLineNotificationRepository
                .findFirstByNotificationStatusAndNotificationType(Notification.Status.NEW, type)
                .isPresent();
    }

    /*** could be used only with sendNotificationsForTodayEvent check */
    private TimeLineNotification getNextNotification(Notification.Type type) {
        TimeLineNotification timeLineNotification
                = timeLineNotificationRepository
                .findFirstByNotificationStatusAndNotificationType(
                        Notification.Status.NEW,
                        type)
                .get();
        timeLineNotification.setStudentProfile(profileService.findProfileByProfileIdLazyWithUserData(timeLineNotification.getStudentProfileId()));
        timeLineNotification.setTimeLineEvent(timeLineService.findTimeLineByTimeLineUUID(timeLineNotification.getTimelineUUID()));
        return timeLineNotification;
    }

    public void createNotificationsFor(Notification.Type type) {
        List<TimeLine> events = new ArrayList<>();
        switch (type) {
            case TODAY_EVENT:
                events = findTimeLineItemsByEventDay(LocalDate.now());
                break;
            case TOMORROW_EVENT:
                events = findTimeLineItemsByEventDay(LocalDate.now().plusDays(1));
                break;
            case REDIRECTION_LINK:
                events = findTimeLineItemsByEventTimeNearTo(LocalDateTime.now());
        }


        for (TimeLine timeLine : events) {
            List<Long> studentsIds = findStreamStudentsProfileIds(timeLine.getStreamTeamProfileId());
            for (Long studentId : studentsIds) {
                createAndSaveTimeLineNotifications(timeLine.getTimelineUUID(), studentId, type);
            }
        }
    }

    private void createAndSaveTimeLineNotifications(String timeLineUUID,
                                                    Long studentProfileId,
                                                    Notification.Type notificationType) {

        TimeLineNotification timeLineNotification = TimeLineNotification.builder()
                .timelineUUID(timeLineUUID)
                .studentProfileId(studentProfileId)
                .notificationType(notificationType)
                .notificationStatus(Notification.Status.NEW)
                .build();

        if (isNotExist(timeLineNotification)) {
            timeLineNotificationRepository.save(timeLineNotification);
        }
    }

    private boolean isNotExist(TimeLineNotification timeLineNotification) {
        return !timeLineNotificationRepository
                .findFirstByTimelineUUIDAndStudentProfileIdAndNotificationType(
                        timeLineNotification.getTimelineUUID(),
                        timeLineNotification.getStudentProfileId(),
                        timeLineNotification.getNotificationType()).isPresent();
    }

    private List<Long> findStreamStudentsProfileIds(Long streamTeamProfileId) throws EntityNotFoundException {
        return profileService.findStudentsProfilesIdForStreamWithSubscriptionStatusAny(streamTeamProfileId);
    }

    @VisibleForTesting
    protected List<TimeLine> findTimeLineItemsByEventDay(LocalDate localDate) {
        return timeLineService.findAllByEventDay(localDate);
    }

    @VisibleForTesting
    protected List<TimeLine> findTimeLineItemsByEventTimeNearTo(LocalDateTime time) {
        return timeLineService.findAllByEventTimeNearTo(time, 70);
    }

    public String makeMessageForNotificationReminder(TimeLine event, Profile student, Notification.Type notificationType) {
        StringBuffer messageText = new StringBuffer();
        messageText.append("Привет, " + student.getUser().getName() + "!");
        messageText.append("\n");
        messageText.append("\n" + notificationType.getSubjectText());
        if(notificationType.equals(Notification.Type.REDIRECTION_LINK)) {
            makeRedirectionLinkText(event, student, messageText);
        }
        messageText.append("\n");

        messageText.append("\nВремя:\t" + event.getDateTime());
        messageText.append("\nФормат:\t" + event.getEventType());
        messageText.append("\nТема:\t" + event.getEventName());
        messageText.append("\nОписание:\t" + event.getNotes());
        messageText.append("\nЧасовой пояс:\tМинск, Беларусь (на 1 час раньше для Украины)");

        messageText.append("\n");
        messageText.append("\nРасписание, ссылки на занятия и записи уроковов профиле группы: https://moodle.jrr.by/profile/" + event.getStreamTeamProfileId());
        messageText.append("\nТам же в профиле группы ссылка на наш чат, где куратор отвечает на организационные вопросы");
        messageText.append("\nТвой куратор: Илья +375(29) 3333-600.");
        messageText.append("\n");

        messageText.append("\nДо встречи на занятиях!");
        messageText.append("\nС уважением,");
        messageText.append("\nУправляющий JavaGuru в Беларуси");
        messageText.append("\nМаксим Шелкович");
        messageText.append("\n+375(44) 750 6666");
        return messageText.toString();
    }

    public void makeRedirectionLinkText(TimeLine event, Profile student, StringBuffer messageText) {
        RedirectionLinkDto.Request request = RedirectionLinkDto.Request.builder() //todo: replace with Mapstruct
                .timelineUUID(event.getTimelineUUID())
                .studentProfileId(student.getId())
                .streamTeamProfileId(event.getStreamTeamProfileId())
                .courseId(event.getCourseId())
                .lectureId(event.getLectureId())
                .urlToRedirect(event.getUrlToRedirect())
                .eventName(event.getEventName())
                .eventType(event.getEventType())
                .expirationMinutes(120)
                .build();
        RedirectionLinkDto.Response link = redirectionLinkService.createRedirectionLink(request);
        messageText.append("\n"+link.getLink());
        messageText.append("\nВНИМАНИЕ! Ссылку можно использовать только один раз.");
        messageText.append("\nЕсли ленивая ссылка недействительна, нужно залогиниться на платформе и использовать ссылку из расписания группы.");
    }
}

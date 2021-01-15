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

import static by.jrr.profile.bean.Notification.Status.*;

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
    public void createAndSendRemindersForLectures() {
        System.out.println("creating And Sending Reminders");
        createNotificationsFor(EventType.LECTURE, Notification.Type.TODAY_EVENT);
        createNotificationsFor(EventType.LECTURE, Notification.Type.TOMORROW_EVENT);

        sendNotificationsFor(Notification.Type.TODAY_EVENT);
        sendNotificationsFor(Notification.Type.TOMORROW_EVENT);
    }

    @Scheduled(cron = "0 */30 * ? * *")
    public void createAndSendRedirectionLinksForLectures() {
        System.out.println("creating And Sending Redirection links for meeting");
        createNotificationsFor(EventType.LECTURE, Notification.Type.REDIRECTION_LINK);

        sendNotificationsFor(Notification.Type.REDIRECTION_LINK);
    }

    @Scheduled(cron = "0 0 12,17 ? * * ")
    public void createAndSendExternalLinkToFeedbackForm() {
        System.out.println("creating And Sending Redirection links for feedback form");
        createNotificationsFor(EventType.FEEDBACK, Notification.Type.FEEDBACK_EXTERNAL_FORM);

        sendNotificationsFor(Notification.Type.FEEDBACK_EXTERNAL_FORM);
    }

    @Scheduled(cron = "0 0 9,12,15,18 ? * * ")
    /*** sends both reminders for future and notifications for past */
    public void createAndSendRemindersForVideoLectures() {
        System.out.println("creating And Sending Redirection links for video lectures");
        createNotificationsFor(EventType.VIDEO, Notification.Type.VIDEO_EVENT);

        sendNotificationsFor(Notification.Type.VIDEO_EVENT);
    }

    private void sendNotificationsFor(Notification.Type type) {
        //todo: create locks for multithreading
        while (nextNotificationIsPresentFor(type)) {
            TimeLineNotification timeLineNotification = getNextNotification(type);
            try {
                sendNotification(timeLineNotification);
                timeLineNotification.setNotificationStatus(SENT);
                timeLineNotificationRepository.save(timeLineNotification);
            } catch (Exception ex) {
                timeLineNotification.setNotificationStatus(ERROR);
                timeLineNotification.setLastErrorMessage(ex.getMessage());
                timeLineNotificationRepository.save(timeLineNotification);
                ex.printStackTrace();
            }
        }
    }

    private void sendNotification(TimeLineNotification timeLineNotification) {
        TimeLine event = timeLineNotification.getTimeLineEvent();
        setDefaultTextIfValueNotPresent(event);

        Profile student = timeLineNotification.getStudentProfile();

        String email = student.getUser().getEmail();
        String subject = "{JG} " + timeLineNotification.getNotificationType().getSubjectText();
        String messageText = new String();
        switch (timeLineNotification.getNotificationType()) {
            case TODAY_EVENT:
            case TOMORROW_EVENT:
            case REDIRECTION_LINK:
                messageText = makeMessageForNotificationReminder(event, student, timeLineNotification.getNotificationType());
                break;
            case FEEDBACK_EXTERNAL_FORM:
                messageText = makeMessageForFeedback(event, student, timeLineNotification.getNotificationType());
                break;
            case VIDEO_EVENT:
                if(event.getDateTime().isAfter(LocalDateTime.now())) {
                    messageText = makeMessageForFutureVideoLecture(event, student, timeLineNotification.getNotificationType());
                    subject = "{JG} Скоро занятие!";
                } else {
                    messageText = makeMessageForPublishedVideo(event, student, timeLineNotification.getNotificationType());
                    subject = "{JG} Доступна видеозапись лекции.";
                }
                break;

        }

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
                .findFirstByNotificationStatusAndNotificationType(NEW, type)
                .isPresent();
    }

    /*** could be used only with sendNotificationsForTodayEvent check */
    private TimeLineNotification getNextNotification(Notification.Type type) {
        TimeLineNotification timeLineNotification
                = timeLineNotificationRepository
                .findFirstByNotificationStatusAndNotificationType(NEW, type)
                .get();
        timeLineNotification.setStudentProfile(profileService.findProfileByProfileIdLazyWithUserData(timeLineNotification.getStudentProfileId()));

        timeLineNotification.setTimeLineEvent(timeLineService.findTimeLineByTimeLineUUID(timeLineNotification.getTimelineUUID()));
        return timeLineNotification;
    }

    private void createNotificationsFor(EventType eventType, Notification.Type notificationType) {
        List<TimeLine> events = new ArrayList<>();
        switch (notificationType) {
            case TODAY_EVENT:
                events = findTimeLineItemsByEventDay(eventType, LocalDate.now());
                break;
            case TOMORROW_EVENT:
                events = findTimeLineItemsByEventDay(eventType, LocalDate.now().plusDays(1));
                break;
            case REDIRECTION_LINK:
                events = findTimeLineItemsByEventTimeNearTo(eventType, LocalDateTime.now());
                break;
            case FEEDBACK_EXTERNAL_FORM:
                events = findTimeLineItemsByEventDayPastDays(eventType, LocalDate.now(), 7);
                break;
            case VIDEO_EVENT:
                events = findTimeLineItemsByEventDayInNextDays(eventType, LocalDate.now(), 4);
                events.addAll(findTimeLineItemsByEventDayPastDays(eventType, LocalDate.now(), 7));
        }


        for (TimeLine timeLine : events) {
            List<Long> studentsIds = findStreamStudentsProfileIds(timeLine.getStreamTeamProfileId());
            for (Long studentId : studentsIds) {
                createAndSaveTimeLineNotifications(timeLine.getTimelineUUID(), studentId, notificationType);
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
                .notificationStatus(NEW)
                .build();

        if (isNotExist(timeLineNotification)) {
            timeLineNotificationRepository.save(timeLineNotification);
        }
    }

    /***
     * Notification considering unic by TimelineUUID & NotificationType for StudentId
     * (composite key of StudentId+TimelineUUID+NotificationType)
     *
     * @param timeLineNotification
     * @return
     */
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
    protected List<TimeLine> findTimeLineItemsByEventDay(EventType type, LocalDate localDate) {
        return timeLineService.findByEventDay(type, localDate);
    }

    @VisibleForTesting  //i.e. to send redirection link for event that starts in about hour
    protected List<TimeLine> findTimeLineItemsByEventTimeNearTo(EventType type, LocalDateTime time) {
        return timeLineService.findByEventTimeNearTo(type, time, 70);
    }

    @VisibleForTesting
    protected List<TimeLine> findTimeLineItemsByEventDayPastDays(EventType type, LocalDate localDate, long daysPast) {
        return timeLineService.findEventsForLastPastDaysFromDate(type, localDate, daysPast);
    }

    @VisibleForTesting
    protected List<TimeLine> findTimeLineItemsByEventDayInNextDays(EventType type, LocalDate localDate, long daysInFuture) {
        return timeLineService.findEventsForFutureDaysFromDate(type, localDate, daysInFuture);
    }

    private String makeMessageForPublishedVideo(TimeLine event, Profile student, Notification.Type notificationType) {
        StringBuffer messageText = new StringBuffer();
        messageText.append("Привет, " + student.getUser().getName() + "!");
        messageText.append("\n");
        messageText.append("\n" + notificationType.getSubjectText());

        messageText.append("\n");
        appendMessageEventData(messageText, event);

        appendMessageFooter(messageText, event);

        return messageText.toString();
    }

    private String makeMessageForFutureVideoLecture(TimeLine event, Profile student, Notification.Type notificationType) {
        StringBuffer messageText = new StringBuffer();
        messageText.append("Привет, " + student.getUser().getName() + "!");
        messageText.append("\n");
        messageText.append("\n" + notificationType.getSubjectText());
        messageText.append("\nСоветую не тянуть до последнего и посмотреть видео лекцию не позднее указанного времени.");

        messageText.append("\n");
        appendMessageEventData(messageText, event);

        appendMessageFooter(messageText, event);

        return messageText.toString();
    }

    private String makeMessageForFeedback(TimeLine event, Profile student, Notification.Type notificationType) {
        StringBuffer messageText = new StringBuffer();
        messageText.append("Привет, " + student.getUser().getName() + "!");
        messageText.append("\n");
        messageText.append("\n" + notificationType.getSubjectText());
        messageText.append("\nПожалуйста, оставь отзыв о лекции и помоги сделать этот курс лучше для тебя.");
        messageText.append("\nВремя:\t" + event.getDateTime());
        messageText.append("\nТема:\t" + event.getEventName());
        messageText.append("\nОписание:\t" + event.getNotes());

        messageText.append("\n");

        messageText.append("\nСсылка на форму обратой связи:");
        int weekInMinutes = 10080;
        makeRedirectionLinkTextWithExpiration(event, student, weekInMinutes, messageText);

        appendMessageFooter(messageText, event);

        return messageText.toString();
    }

    private String makeMessageForNotificationReminder(TimeLine event, Profile student, Notification.Type notificationType) {
        StringBuffer messageText = new StringBuffer();
        messageText.append("Привет, " + student.getUser().getName() + "!");
        messageText.append("\n");
        messageText.append("\n" + notificationType.getSubjectText());
        if(notificationType.equals(Notification.Type.REDIRECTION_LINK)) {
            makeRedirectionLinkTextWithExpiration(event, student, 120, messageText);
        }
        messageText.append("\n");

        appendMessageEventData(messageText, event);
        messageText.append("\nЧасовой пояс:\tМинск, Беларусь (на 1 час раньше для Украины)");

        appendMessageFooter(messageText, event);

        return messageText.toString();
    }

    private void makeRedirectionLinkTextWithExpiration(TimeLine event, Profile student, int expirationInMinutes, StringBuffer messageText) {
        RedirectionLinkDto.Request request = RedirectionLinkDto.Request.builder() //todo: replace with Mapstruct
                .timelineUUID(event.getTimelineUUID())
                .studentProfileId(student.getId())
                .streamTeamProfileId(event.getStreamTeamProfileId())
                .courseId(event.getCourseId())
                .lectureId(event.getLectureId())
                .urlToRedirect(event.getUrlToRedirect())
                .eventName(event.getEventName())
                .eventType(event.getEventType())
                .expirationMinutes(expirationInMinutes)
                .build();
        RedirectionLinkDto.Response link = redirectionLinkService.createRedirectionLink(request);
        messageText.append("\n"+link.getLink());
        messageText.append("\nВНИМАНИЕ! Ссылку можно использовать только один раз.");
        messageText.append("\nЕсли ленивая ссылка недействительна, нужно залогиниться на платформе и использовать ссылку из расписания группы.");
    }

    private void appendMessageFooter(StringBuffer messageText, TimeLine event) {
        messageText.append("\n");
        messageText.append("\nРасписание, ссылки на занятия и записи уроково в профиле группы: https://moodle.jrr.by/profile/" + event.getStreamTeamProfileId());
        messageText.append("\nТам же в профиле группы ссылка на наш чат, где куратор отвечает на организационные вопросы");
        messageText.append("\nТвой куратор: Илья +375(29) 3333-600.");
        messageText.append("\n");

        messageText.append("\nДо встречи на занятиях!");
        messageText.append("\nС уважением,");
        messageText.append("\nУправляющий JavaGuru в Беларуси");
        messageText.append("\nМаксим Шелкович");
        messageText.append("\n+375(44) 750 6666");
    }

    private void appendMessageEventData(StringBuffer messageText, TimeLine event) {
        messageText.append("\nВремя:\t" + event.getDateTime());
        messageText.append("\nФормат:\t" + event.getEventType());
        messageText.append("\nТема:\t" + event.getEventName());
        messageText.append("\nОписание:\t" + event.getNotes());
    }
}

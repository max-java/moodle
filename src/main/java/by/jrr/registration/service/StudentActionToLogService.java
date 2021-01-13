package by.jrr.registration.service;

import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfileService;
import by.jrr.profile.service.StreamAndTeamSubscriberService;
import by.jrr.registration.bean.EventType;
import by.jrr.registration.bean.StudentActionToLog;
import by.jrr.registration.controller.LogActionAndRedirectController;
import by.jrr.registration.repository.StudentActionToLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentActionToLogService {

    @Autowired
    StudentActionToLogRepository satlR;
    @Autowired
    ProfileService profileService;
    @Autowired
    StreamAndTeamSubscriberService streamAndTeamSubscriberService;

    public void saveAction(Long streamTeamId, String eventType, String link, String linkName) {

        satlR.save(
                StudentActionToLog.builder()
                        .streamTeamProfileId(streamTeamId)
                        .studentProfileId(profileService.getCurrentUserProfileId())
                        .eventType(EventType.valueOf(eventType))
                        .eventName(linkName)
                        .timestamp(LocalDateTime.now())
                        .urlToRedirect(link)
                        .build()
        );
    }

    public void saveAction(StudentActionToLog studentActionToLog) {
        studentActionToLog.setTimestamp(LocalDateTime.now());
        if(studentActionToLog.getStudentProfileId() == null) {
            studentActionToLog.setStudentProfileId(profileService.getCurrentUserProfileId());
        }
        satlR.save(studentActionToLog);
    }

    public LogActionAndRedirectController.UserActivityDTO findActionForStreamBetweenTimestamps(Long streamProfileId, LocalDateTime start, LocalDateTime end) {
        LogActionAndRedirectController.UserActivityDTO userActivityDTO = new LogActionAndRedirectController.UserActivityDTO();
        List<StudentActionToLog> studentActionToLogList = satlR.findAllByStreamTeamProfileIdAndTimestampBetween(streamProfileId, start,end);
        List<LogActionAndRedirectController.UserActivityElementDTO> active = studentActionToLogList.stream()
                .map(a -> a.getStudentProfileId())
                .map(id -> profileService.findProfileByProfileId(id).orElseGet(Profile::new))
                .map(profile -> setDTO(profile))
                .collect(Collectors.toList());

        List<LogActionAndRedirectController.UserActivityElementDTO> subscribers = streamAndTeamSubscriberService.getAllSubscribersForProfileByProfileId(streamProfileId).stream()
                .map(sub -> sub.getSubscriberProfileId())
                .map(id -> profileService.findProfileByProfileId(id).orElseGet(Profile::new))
                .map(profile -> setDTO(profile))
                .collect(Collectors.toList());

        List<LogActionAndRedirectController.UserActivityElementDTO> notActiveOfSubscribers = new ArrayList<>();
        for (LogActionAndRedirectController.UserActivityElementDTO sub : subscribers) {
            if (active.stream().noneMatch(a -> a.getUserName().equals(sub.getUserName()))) {
                notActiveOfSubscribers.add(sub);
            }
        }

        notActiveOfSubscribers = notActiveOfSubscribers.stream()
                .distinct()
                .collect(Collectors.toList());
        active = active.stream()
                .distinct()
                .collect(Collectors.toList());

        userActivityDTO.setActive(active);
        userActivityDTO.setNotActive(notActiveOfSubscribers);
        return userActivityDTO;
    }

    private LogActionAndRedirectController.UserActivityElementDTO setDTO(Profile profile) {
        LogActionAndRedirectController.UserActivityElementDTO elementDTO = new LogActionAndRedirectController.UserActivityElementDTO();
        if(profile != null && profile.getUser() != null) {
            elementDTO.setFirstName(profile.getUser().getName());
            elementDTO.setLastName(profile.getUser().getLastName());
            elementDTO.setEmail(profile.getUser().getEmail());
            elementDTO.setPhone(profile.getUser().getPhone());
            elementDTO.setUserName(profile.getUser().getUserName());
            elementDTO.setProfileLink("/admin/profile/" + profile.getId());

        } else {
            elementDTO.setFirstName(".");
            elementDTO.setLastName(".");
            elementDTO.setEmail(".");
            elementDTO.setPhone(".");
            elementDTO.setUserName(".");
            elementDTO.setProfileLink(".");
        }
        //костыль для тех, кто зарегался, когда фелефон был необязательным полем (для того, что бы на хендлить каждое значение на стороне JS)
        if (elementDTO.getPhone() == null) {
            elementDTO.setPhone(".");
        }
        //и на всякий случай для всех остальных полей
        if (elementDTO.getFirstName() == null) {
            elementDTO.setFirstName(".");
        }
        if (elementDTO.getLastName() == null) {
            elementDTO.setLastName(".");
        }
        if (elementDTO.getEmail() == null) {
            elementDTO.setEmail(".");
        }
        if (elementDTO.getUserName() == null) {
            elementDTO.setUserName(".");
        }
        if (elementDTO.getProfileLink() == null) {
            elementDTO.setProfileLink(".");
        }

        return elementDTO;
    }

    public List<StudentActionToLog> findAllActionsForProfileId(Long profileId) {
        return satlR.findAllBystudentProfileId(profileId);
    }

    public List<StudentActionToLog> findAllActionsForProfileIdBetwenDates(Long profileId, LocalDateTime start, LocalDateTime finish) {
        return satlR.findAllBystudentProfileIdAndTimestampBetween(profileId, start, finish);
    }

    public List<LogActionAndRedirectController.UserActivityElementDTO> convertUserActivityListToDTO(List<StudentActionToLog> studentActionToLogList) {
        List<LogActionAndRedirectController.UserActivityElementDTO> dtoList = new ArrayList<>();

        for (StudentActionToLog studentActionToLog : studentActionToLogList) {

            LogActionAndRedirectController.UserActivityElementDTO dto = new LogActionAndRedirectController.UserActivityElementDTO();
            dto.setEventType(studentActionToLog.getEventType().name());
            dto.setEventName(studentActionToLog.getEventName());
            dto.setTimestamp(studentActionToLog.getTimestamp().toString());

            if(dto.getEventType() == null) {
                dto.setEventType(".");
            }
            if(dto.getEventName() == null) {
                dto.setEventName(".");
            }
            if(dto.getTimestamp() == null) {
                dto.setTimestamp(".");
            }
            dtoList.add(dto);
        }
        return dtoList;
    }

    public Optional<StudentActionToLog> findLastActionBeforeTimestamp(LocalDateTime timestamp) {// TODO: 06/08/20 save logs with stream / team id, find last log row for stream
        List<StudentActionToLog> studentActionToLogList = satlR.findAllByTimestampBetween(timestamp.minusSeconds(30), timestamp);
        if (studentActionToLogList.size() > 0) {
            return Optional.of(studentActionToLogList.get(studentActionToLogList.size()-1));
        }
        return Optional.empty();
    }
}

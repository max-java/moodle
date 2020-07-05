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
import java.util.stream.Collectors;

@Service
public class StudentActionToLogService {

    @Autowired
    StudentActionToLogRepository satlR;
    @Autowired
    ProfileService profileService;
    @Autowired
    StreamAndTeamSubscriberService streamAndTeamSubscriberService;

    public void saveAction(Long streamTeamId, String eventType, String link) {

        satlR.save(
                StudentActionToLog.builder()
                        .streamTeamProfileId(streamTeamId)
                        .studentProfileId(profileService.getCurrentUserProfileId())
                        .eventType(EventType.valueOf(eventType))
                        .timestamp(LocalDateTime.now())
                        .urlToRedirect(link)
                        .build()
        );
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
        } else {
            elementDTO.setFirstName(".");
            elementDTO.setLastName(".");
            elementDTO.setEmail(".");
            elementDTO.setPhone(".");
            elementDTO.setUserName(".");
        }
        //костыль для тех, кто зарегался, когда фелефон был необязательным полем (для того, что бы на хендлить каждое значение на стороне JS)
        if (elementDTO.getPhone() == null) {
            elementDTO.setPhone(".");
        }
        return elementDTO;
    }
}

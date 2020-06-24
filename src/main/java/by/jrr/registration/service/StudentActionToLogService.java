package by.jrr.registration.service;

import by.jrr.profile.service.ProfileService;
import by.jrr.registration.bean.EventType;
import by.jrr.registration.bean.StudentActionToLog;
import by.jrr.registration.repository.StudentActionToLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StudentActionToLogService {

    @Autowired
    StudentActionToLogRepository satlR;
    @Autowired
    ProfileService profileService;

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
}

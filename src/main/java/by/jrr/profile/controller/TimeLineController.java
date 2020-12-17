package by.jrr.profile.controller;

import by.jrr.balance.constant.FieldName;
import by.jrr.balance.constant.FormCommand;
import by.jrr.constant.Endpoint;
import by.jrr.profile.bean.TimeLine;
import by.jrr.profile.service.TimeLineService;
import by.jrr.registration.bean.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class TimeLineController {
    @Autowired
    TimeLineService timeLineService;

    @PostMapping(Endpoint.PROFILE_TIMELINE)
    public String saveTimeLine(@RequestParam Long streamTeamProfileId,
                               @RequestParam(value = "dateTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                               @RequestParam(value = "courseId", required = false) Long courseId,
                               @RequestParam(value = "eventType", required = false) EventType eventType,
                               @RequestParam(value = "urlToRedirect", required = false) String urlToRedirect,
                               @RequestParam(value = "lectureId", required = false) Long lectureId,
                               @RequestParam(value = "eventName", required = false) String eventName,
                               @RequestParam(value = "notes", required = false) String notes,
                               @RequestParam(value = FieldName.FORM_COMMAND, required = false) FormCommand command,
                               @RequestParam(value = "timelineId", required = false) Long timelineId
    ) {
        switch (command) {
            case SAVE:
                timeLineService.save(TimeLine.builder()
                        .Id(timelineId)
                        .streamTeamProfileId(streamTeamProfileId)
                        .dateTime(dateTime)
                        .courseId(courseId)
                        .eventType(eventType)
                        .urlToRedirect(urlToRedirect.trim())
                        .lectureId(lectureId)
                        .eventName(eventName)
                        .notes(notes)
                        .build());
                EventType.LECTURE.equals(eventType);
                break;
            case DELETE: // todo: replace with form command
                timeLineService.delete(timelineId);
                break;
        }

        return "redirect:" + Endpoint.PROFILE_CARD + "/" + streamTeamProfileId;
    }
}

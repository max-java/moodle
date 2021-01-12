package by.jrr.registration.mapper;

import by.jrr.registration.bean.EventType;
import by.jrr.registration.bean.RedirectionLink;
import by.jrr.registration.bean.StudentActionToLog;
import by.jrr.registration.model.RedirectionLinkDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RedirectionLinkMapperTest {

    @Test
    void getRedirectionLinkFromRequest() {
        RedirectionLink redirectionLink = RedirectionLinkMapper.OF.getRedirectionLinkFromRequest(makeRequest());
        assertEquals(makeRedirectionLink(), redirectionLink);
    }

    @Test
    void  getStudentActionToLogFromRedirectionLink() {
        StudentActionToLog studentActionToLog =
                RedirectionLinkMapper.OF.getStudentActionToLogFromRedirectionLink(makeRedirectionLinkTimestamped());
        assertEquals(makeStudentActionToLog(), studentActionToLog);
    }

    private RedirectionLinkDto.Request makeRequest() {
        RedirectionLinkDto.Request request = new RedirectionLinkDto.Request();
        request.setCourseId(1L);
        request.setLectureId(2L);
        request.setStreamTeamProfileId(3L);
        request.setStudentProfileId(4L);
        request.setEventName("event name");
        request.setEventType(EventType.LECTURE);
        request.setExpirationMinutes(15);
        request.setUrlToRedirect("https://UrlToRedirect.com");
        return request;
    }

    private RedirectionLink makeRedirectionLink() {
        RedirectionLink redirectionLink = new RedirectionLink();
        redirectionLink.setCourseId(1L);
        redirectionLink.setLectureId(2L);
        redirectionLink.setStreamTeamProfileId(3L);
        redirectionLink.setStudentProfileId(4L);
        redirectionLink.setEventName("event name");
        redirectionLink.setEventType(EventType.LECTURE);
        redirectionLink.setExpirationMinutes(15);
        redirectionLink.setUrlToRedirect("https://UrlToRedirect.com");
        return redirectionLink;
    }

    private StudentActionToLog makeStudentActionToLog() {
        StudentActionToLog studentActionToLog = new StudentActionToLog();
        studentActionToLog.setCourseId(1L);
        studentActionToLog.setLectureId(2L);
        studentActionToLog.setStreamTeamProfileId(3L);
        studentActionToLog.setStudentProfileId(4L);
        studentActionToLog.setEventName("event name");
        studentActionToLog.setEventType(EventType.LECTURE);
        studentActionToLog.setUrlToRedirect("https://UrlToRedirect.com");

        studentActionToLog.setTimestamp(null);
        return studentActionToLog;
    }

    private RedirectionLink makeRedirectionLinkTimestamped() {
        RedirectionLink redirectionLink = makeRedirectionLink();
        redirectionLink.setTimestamp(LocalDateTime.now());
        return redirectionLink;
    }
}

package by.jrr.registration.model;

import by.jrr.registration.bean.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CreateRedirectionLink {

    @Data
    @NoArgsConstructor
    public static class Request {
        private Long studentProfileId;
        private Long streamTeamProfileId;
        private Long courseId;
        private Long lectureId;
        private String urlToRedirect;
        private String eventName;
        private EventType eventType;
        private int expirationMinutes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String link;
    }
}

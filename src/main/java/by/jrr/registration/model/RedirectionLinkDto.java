package by.jrr.registration.model;

import by.jrr.registration.bean.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RedirectionLinkDto {

    @Data
    @NoArgsConstructor
    public static class Request {
        private String timelineUUID;  //todo: because timeline events has uuid, consider simplifying process based on that.
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

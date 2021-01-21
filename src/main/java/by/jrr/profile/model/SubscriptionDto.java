package by.jrr.profile.model;

import by.jrr.profile.bean.SubscriptionStatus;
import lombok.Data;


public class SubscriptionDto {

    @Data
    public static class Request {
        private Long streamTeamProfileId;
        private Long subscriberProfileId;
        private SubscriptionStatus status;
        private String notes;
    }

    @Data
    public static class Response {
        private String notes;
    }


}

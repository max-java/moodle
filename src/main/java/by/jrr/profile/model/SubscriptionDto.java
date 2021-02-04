package by.jrr.profile.model;

import by.jrr.profile.bean.SubscriptionStatus;
import lombok.Data;


public class SubscriptionDto {
    public static final String FIELD_STREAM_PROFILE_ID = "streamTeamProfileId";
    public static final String FIELD_USER_PROFILE_ID = "subscriberProfileId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_NOTES = "notes";

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

package by.jrr.profile.mapper;

import by.jrr.profile.bean.SubscriptionStatus;
import by.jrr.profile.model.SubscriptionDto;
import org.mapstruct.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

public class SubscriptionParamMap {
    @Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    public @interface UserProfileId {
    }

    @UserProfileId
    public Long userProfileId(Map<String, String> paramMap) {
        Long result = null;
        try {
            return Long.valueOf(paramMap.get(SubscriptionDto.FIELD_USER_PROFILE_ID));
        } catch (Exception ex) {
            String.format("Can't convert %s to %s", paramMap.get(SubscriptionDto.FIELD_USER_PROFILE_ID), Long.class.getSimpleName());
            return result;
        }
    }

    @Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    public @interface StreamTeamProfileId {
    }

    @StreamTeamProfileId
    public Long streamTeamProfileId(Map<String, String> paramMap) {
        Long result = null;
        try {
            return Long.valueOf(paramMap.get(SubscriptionDto.FIELD_STREAM_PROFILE_ID));
        } catch (Exception ex) {
            String.format("Can't convert %s to %s", paramMap.get(SubscriptionDto.FIELD_STREAM_PROFILE_ID), Long.class.getSimpleName());
            return result;
        }
    }

    @Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {
    }

    @Status
    public SubscriptionStatus status(Map<String, String> paramMap) {
        try {
            return SubscriptionStatus.valueOf(paramMap.get(SubscriptionDto.FIELD_STATUS));
        } catch (Exception ex) {
            String.format("Can't convert %s to %s", paramMap.get(SubscriptionDto.FIELD_STATUS), SubscriptionStatus.class.getSimpleName());
            return null;
        }
    }

    @Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    public @interface Notes {
    }

    @Notes
    public String notes(Map<String, String> paramMap) {
        return paramMap.get(SubscriptionDto.FIELD_NOTES);
    }
}

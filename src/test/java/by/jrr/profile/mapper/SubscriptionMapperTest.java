package by.jrr.profile.mapper;

import by.jrr.profile.bean.StreamAndTeamSubscriber;
import by.jrr.profile.bean.SubscriptionStatus;
import by.jrr.profile.model.SubscriptionDto;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubscriptionMapperTest {


    @Test
    void getStreamTeamSubscriberFrom() {
        StreamAndTeamSubscriber subscriber = SubscriptionMapper.OF.getStreamTeamSubscriberFrom(makeRequest());
        assertEquals(makeStreamTeamSubscriber(), subscriber);
    }

    @Test
    void getSubscriptionRequestFromMap() {
        SubscriptionDto.Request actualRequest = SubscriptionMapper.OF.getSubscriptionRequestFromMap(makeMap());
        assertEquals(makeRequest(), actualRequest);
    }


    private SubscriptionDto.Request makeRequest() {
        SubscriptionDto.Request request = new SubscriptionDto.Request();
        request.setStreamTeamProfileId(3L);
        request.setSubscriberProfileId(4L);
        request.setStatus(SubscriptionStatus.REQUESTED);
        request.setNotes("Notes");
        return request;
    }

    private StreamAndTeamSubscriber makeStreamTeamSubscriber() {
        StreamAndTeamSubscriber redirectionLink = new StreamAndTeamSubscriber();
        redirectionLink.setStatus(SubscriptionStatus.REQUESTED);
        redirectionLink.setStreamTeamProfileId(3L);
        redirectionLink.setSubscriberProfileId(4L);
        return redirectionLink;
    }

    private Map<String, String> makeMap() {
        Map<String, String> map = new HashMap<>();
        map.put(SubscriptionDto.FIELD_USER_PROFILE_ID, "4");
        map.put(SubscriptionDto.FIELD_STREAM_PROFILE_ID, "3");
        map.put(SubscriptionDto.FIELD_STATUS, SubscriptionStatus.REQUESTED.name());
        map.put(SubscriptionDto.FIELD_NOTES, "Notes");
        return map;
    }
}

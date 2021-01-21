package by.jrr.profile.mapper;

import by.jrr.profile.bean.StreamAndTeamSubscriber;
import by.jrr.profile.bean.SubscriptionStatus;
import by.jrr.profile.model.SubscriptionDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubscriptionMapperTest {


    @Test
    void getStreamTeamSubscriberFrom() {
        StreamAndTeamSubscriber subscriber = SubscriptionMapper.OF.getStreamTeamSubscriberFrom(makeRequest());
        assertEquals(makeStreamTeamSubscriber(), subscriber);
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
}

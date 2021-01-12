package by.jrr.registration.service;

import by.jrr.registration.bean.EventType;
import by.jrr.registration.bean.RedirectionLink;
import by.jrr.registration.bean.RedirectionLinkStatus;
import by.jrr.registration.model.RedirectionLinkDto;
import by.jrr.registration.repository.RedirectionLinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/registration.sql")
@SpringBootTest
class RedirectionLinkServiceIntTest {

    @Autowired
    RedirectionLinkService redirectionLinkService;
    @Autowired
    RedirectionLinkRepository redirectionLinkRepository;

    @BeforeEach
    public void setUp() {

    }

    @Test
    void createRedirectionLink() {
        RedirectionLinkDto.Response result = redirectionLinkService.createRedirectionLink(makeRequest());
        assertThat(result.getLink()).startsWith("https://moodle.jrr.by/redirect/");
        assertThat(result.getLink()).hasSize(67);

        String uuid = result.getLink().substring("https://moodle.jrr.by/redirect/".length());
        RedirectionLink generatedLink = redirectionLinkRepository.findById(uuid).get();
        assertThat(generatedLink.getRedirectionPage()).isEqualTo("https://moodle.jrr.by/redirect/"+uuid);
    }

    @Test
    void useRedirectionLink_shouldSetExpired() {
        redirectionLinkService.setClock(Clock.fixed(Instant.parse("2020-12-31T23:59:59.00Z"), ZoneId.of("UTC")));

        RedirectionLink result = redirectionLinkService.useRedirectionLink("expired-aaaa-aaaa-aaaa");

        assertThat(result.getStatus()).isEqualTo(RedirectionLinkStatus.EXPIRED);
        assertThat(result.getUrlToRedirect()).isEmpty();

        RedirectionLinkStatus status = redirectionLinkRepository.findById("expired-aaaa-aaaa-aaaa").get().getStatus();
        assertThat(status).isEqualTo(RedirectionLinkStatus.EXPIRED);
    }

    @Test
    void useRedirectionLink_shouldSetUsed() {
        redirectionLinkService.setClock(Clock.fixed(Instant.parse("2020-12-31T23:59:59.00Z"), ZoneId.of("UTC")));

        RedirectionLink result = redirectionLinkService.useRedirectionLink("working-aaaa-aaaa-bbbb");

        assertThat(result.getStatus()).isEqualTo(RedirectionLinkStatus.NEW);

        RedirectionLinkStatus status = redirectionLinkRepository.findById("working-aaaa-aaaa-bbbb").get().getStatus();
        assertThat(status).isEqualTo(RedirectionLinkStatus.USED);
    }

    @Test
    void useRedirectionLink_shouldEraseLinkToRedirect() {
        RedirectionLink result1 = redirectionLinkService.useRedirectionLink("used-aaaa-aaaa-aaaa");
        RedirectionLink result2 = redirectionLinkService.useRedirectionLink("already-expired-aaaa-aaaa");

        assertThat(result1.getStatus()).isEqualTo(RedirectionLinkStatus.USED);
        assertThat(result1.getUrlToRedirect()).isEmpty();

        assertThat(result2.getStatus()).isEqualTo(RedirectionLinkStatus.EXPIRED);
        assertThat(result2.getUrlToRedirect()).isEmpty();

        RedirectionLinkStatus status1 = redirectionLinkRepository.findById("used-aaaa-aaaa-aaaa").get().getStatus();
        RedirectionLinkStatus status2 = redirectionLinkRepository.findById("already-expired-aaaa-aaaa").get().getStatus();

        assertThat(status1).isEqualTo(RedirectionLinkStatus.USED);
        assertThat(status2).isEqualTo(RedirectionLinkStatus.EXPIRED);
    }

    @Test
    void findRedirectionLinksForUserByProfileId() {
        List<RedirectionLink> result = redirectionLinkService.findRedirectionLinksForUserByProfileId(3L);
        assertThat(result).hasSize(2);
    }

    @Test
    void findRedirectionLinksForStreamByStreamId() {
        List<RedirectionLink> result = redirectionLinkService.findRedirectionLinksForStreamByStreamId(4L);
        assertThat(result).hasSize(2);
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

}

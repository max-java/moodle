package by.jrr.registration.service;

import by.jrr.common.annotations.VisibleForTesting;
import by.jrr.registration.bean.RedirectionLink;
import by.jrr.registration.bean.RedirectionLinkStatus;
import by.jrr.registration.mapper.RedirectionLinkMapper;
import by.jrr.registration.model.CreateRedirectionLink;
import by.jrr.registration.repository.RedirectionLinkRepository;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RedirectionLinkService {

    @Autowired
    RedirectionLinkRepository redirectionLinkRepository;

    Clock clock = Clock.systemDefaultZone();

    @VisibleForTesting
    public void setClock(Clock clock) {
        this.clock = clock;
    }

    @VisibleForTesting
    public void setDefaultClock() {
        this.clock = Clock.systemDefaultZone();
    }

    private static final String REDIRECTION_PAGE_BASE_URL = "https://moodle.jrr.by/redirect/";
    private static final int DEFAULT_EXPIRATION = 15;


    public CreateRedirectionLink.Response createRedirectionLink(CreateRedirectionLink.Request request) {
        String uuid = UUID.randomUUID().toString();
        String redirectionPage = REDIRECTION_PAGE_BASE_URL.concat(uuid);

        RedirectionLink redirectionLink = RedirectionLinkMapper.OF.getRedirectionLinkFromRequest(request);

        redirectionLink.setExpirationMinutes(Optional.ofNullable(redirectionLink.getExpirationMinutes()).orElse(DEFAULT_EXPIRATION));
        redirectionLink.setUuid(uuid);
        redirectionLink.setRedirectionPage(redirectionPage);
        redirectionLink.setStatus(RedirectionLinkStatus.NEW);
        redirectionLinkRepository.save(redirectionLink);

        return new CreateRedirectionLink.Response(redirectionPage);
    }

    @Transactional
    public RedirectionLink useRedirectionLink(String uuid) {
        RedirectionLink redirectionLink = redirectionLinkRepository.findById(uuid).orElseThrow(EntityNotFoundException::new);

        if (isUsed(redirectionLink) || isHasBeenExpiredAlready(redirectionLink)) {
            clearLink(redirectionLink);
        } else if (isExpired(redirectionLink)) {
            markLinkExpired(redirectionLink);
            redirectionLink.setStatus(RedirectionLinkStatus.EXPIRED);
            clearLink(redirectionLink);
        } else {
            markLinkUsed(redirectionLink);
        }
        return redirectionLink;
    }

    public List<RedirectionLink> findRedirectionLinksForUserByProfileId(Long profileId) {
        return (List) redirectionLinkRepository.findAllByStudentProfileIdOrderByTimestamp(profileId);
    }
    public List<RedirectionLink> findRedirectionLinksForStreamByStreamId(Long streamId) {
        return (List) redirectionLinkRepository.findAllByStreamTeamProfileIdOrderByTimestamp(streamId);
    }


    private void clearLink(RedirectionLink redirectionLink) {
        redirectionLink.setUrlToRedirect("");
    }

    private boolean isUsed(RedirectionLink redirectionLink) {
        return redirectionLink.getStatus().equals(RedirectionLinkStatus.USED);
    }

    private boolean isExpired(RedirectionLink redirectionLink) {
        return redirectionLink.getTimestamp().plusMinutes(redirectionLink.getExpirationMinutes())
            .isBefore(LocalDateTime.now(clock));
    }

    private boolean isHasBeenExpiredAlready(RedirectionLink redirectionLink) {
        return redirectionLink.getStatus().equals(RedirectionLinkStatus.EXPIRED);
    }


    protected void markLinkUsed(RedirectionLink redirectionLink) {
        redirectionLinkRepository.updateStatus(RedirectionLinkStatus.USED.name(), redirectionLink.getUuid());
    }


    protected void markLinkExpired(RedirectionLink redirectionLink) {
        redirectionLinkRepository.updateStatus(RedirectionLinkStatus.EXPIRED.name(), redirectionLink.getUuid());
    }
}

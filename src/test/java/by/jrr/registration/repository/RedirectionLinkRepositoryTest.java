package by.jrr.registration.repository;

import by.jrr.config.IntegrationTest;
import by.jrr.registration.bean.RedirectionLink;
import by.jrr.registration.bean.RedirectionLinkStatus;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql("/registration.sql")
class RedirectionLinkRepositoryTest extends IntegrationTest {

    @Resource
    RedirectionLinkRepository redirectionLinkRepository;

    @Test
    public void saveAndFindByUuid(){
        RedirectionLink redirectionLink = new RedirectionLink();
        String uuid = UUID.randomUUID().toString();
        redirectionLink.setUuid(uuid);
        redirectionLinkRepository.save(redirectionLink);
        RedirectionLink savedRedirection = redirectionLinkRepository.findById(uuid).get();
        assertEquals(uuid, savedRedirection.getUuid());

    }

    @Test
    @Transactional
    public void updateStatus() {
        redirectionLinkRepository.updateStatus(RedirectionLinkStatus.USED.name(), "expired-aaaa-aaaa-aaaa");
        RedirectionLink result = redirectionLinkRepository.findById("expired-aaaa-aaaa-aaaa").get();
        assertEquals(RedirectionLinkStatus.USED, result.getStatus());
    }
}

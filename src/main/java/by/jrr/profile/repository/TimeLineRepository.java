package by.jrr.profile.repository;

import by.jrr.profile.bean.TimeLine;
import by.jrr.registration.bean.EventType;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeLineRepository extends PagingAndSortingRepository<TimeLine, Long> {
    List<TimeLine> findAllByStreamTeamProfileId(Long id);
    TimeLine findByTimelineUUID(String uuid);
    List<TimeLine> findAllByTimelineUUID(String uuid);
    Iterable<TimeLine> findAllByDateTimeBetween(LocalDateTime from, LocalDateTime till);
    Iterable<TimeLine> findAllByEventTypeAndDateTimeBetween(EventType type, LocalDateTime from, LocalDateTime till);
}

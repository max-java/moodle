package by.jrr.profile.repository;

import by.jrr.profile.bean.TimeLine;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeLineRepository extends PagingAndSortingRepository<TimeLine, Long> {
    List<TimeLine> findAllByStreamTeamProfileId(Long id);
    TimeLine findByTimelineUUID(String uuid);
    Iterable<TimeLine> findAllByDateTimeBetween(LocalDateTime from, LocalDateTime till);
}

package by.jrr.registration.repository;

import by.jrr.registration.bean.StudentActionToLog;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StudentActionToLogRepository extends PagingAndSortingRepository<StudentActionToLog, Long> {
    List<StudentActionToLog> findAllByTimestampBetween(LocalDateTime start, LocalDateTime finish);
    List<StudentActionToLog> findAllByStreamTeamProfileIdAndTimestampBetween(Long id, LocalDateTime start, LocalDateTime finish);
    List<StudentActionToLog> findAllBystudentProfileIdAndTimestampBetween(Long id, LocalDateTime start, LocalDateTime finish);
    List<StudentActionToLog> findAllBystudentProfileId(Long id);
    List<StudentActionToLog> findAllByTimelineUUID(String uuid);
    List<StudentActionToLog> findAllByTimelineUUIDAndTimestampBetween(String uuid, LocalDateTime start, LocalDateTime finish);

}

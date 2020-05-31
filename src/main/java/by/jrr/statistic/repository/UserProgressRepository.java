package by.jrr.statistic.repository;

import by.jrr.statistic.bean.UserProgress;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends PagingAndSortingRepository<UserProgress, Long> {
    Optional<UserProgress> findByTrackableIdAndProfileId(Long trackableId, Long profileId);
    List<UserProgress> findByProfileId(Long id);
}

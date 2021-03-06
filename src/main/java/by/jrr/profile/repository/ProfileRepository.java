package by.jrr.profile.repository;

import by.jrr.profile.bean.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends PagingAndSortingRepository<Profile, Long>{
    Optional<Profile> findByUserId(Long aLong);
    List<Profile> findAllByUserIdIn(Iterable<Long> userId);

    List<Profile> findAllByCourseIdAndDateStartAfter(Long courseId, LocalDate date);
    List<Profile> findAllByCourseId(Long courseId);
    List<Profile> findAllByCourseIdNotNull();
    List<Profile> findAllByCourseIdNotNullAndDateStartIsBeforeAndDateEndIsAfter(LocalDate now1, LocalDate now2);
    List<Profile> findAllByCourseIdAndOpenForEnroll(Long courseId, Boolean openForEnroll);
    List<Profile> findAllByOpenForEnroll(Boolean openForEnroll);
}


package by.jrr.profile.repository;

import by.jrr.profile.bean.Profile;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends PagingAndSortingRepository<Profile, Long>{
    Optional<Profile> findByUserId(Long aLong);
    List<Profile> findAllByUserIdIn(Iterable<Long> userId);
}


package by.jrr.profile.repository;

import by.jrr.profile.bean.ProfilePossesses;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfilePossessesRepository extends PagingAndSortingRepository<ProfilePossesses, Long>{

    Optional<ProfilePossesses> findByProfileIdAndEntityId(Long profileId, Long entityId);
}


package by.jrr.profile.repository;

import by.jrr.profile.bean.StreamAndTeamSubscriber;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StreamAndTeamSubscriberRepository extends PagingAndSortingRepository<StreamAndTeamSubscriber, Long>{
    List<StreamAndTeamSubscriber> findAllByStreamTeamProfileId(Long id);
    Optional<StreamAndTeamSubscriber> findAllByStreamTeamProfileIdAndSubscriberProfileId(Long idL, Long idR);

}


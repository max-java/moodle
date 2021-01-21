package by.jrr.profile.repository;

import by.jrr.profile.bean.StreamAndTeamSubscriber;
import by.jrr.profile.bean.SubscriptionStatus;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StreamAndTeamSubscriberRepository extends PagingAndSortingRepository<StreamAndTeamSubscriber, Long>{
    List<StreamAndTeamSubscriber> findAllByStreamTeamProfileId(Long id);
    List<StreamAndTeamSubscriber> findAllBySubscriberProfileId(Long id);
    Optional<StreamAndTeamSubscriber> findAllByStreamTeamProfileIdAndSubscriberProfileId(Long idL, Long idR);
    Optional<StreamAndTeamSubscriber> findBySubscriberProfileIdAndStreamTeamProfileId(Long userProfileId, Long streamProfileId);


    List<StreamAndTeamSubscriber> findAllByStatus(SubscriptionStatus status);
}


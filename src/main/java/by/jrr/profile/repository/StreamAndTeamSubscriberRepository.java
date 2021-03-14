package by.jrr.profile.repository;

import by.jrr.profile.bean.StreamAndTeamSubscriber;
import by.jrr.profile.bean.SubscriptionStatus;
import org.springframework.data.jpa.repository.Query;
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

    @Query(value = "select * from GROUP_VIEW natural join SUBSCRIPTIONS_STATISTIC_VIEW", nativeQuery = true)
    List<SubscriptionsStatisticViewDto> getSubscriptionsStatistic();

}

//    CREATE VIEW SUBSCRIPTIONS_APPROVED_VIEW AS SELECT stream_team_profile_id, status, count(id) as TOTAL_APPROVED from stream_and_team_subscriber where status = 'APPROVED' group by stream_team_profile_id;
//    CREATE VIEW SUBSCRIPTIONS_CANCELED_VIEW AS SELECT stream_team_profile_id, status, count(id) as TOTAL_CANCELED from stream_and_team_subscriber where status = 'CANCELED' group by stream_team_profile_id;
//    CREATE VIEW SUBSCRIPTIONS_REJECTED_VIEW AS SELECT stream_team_profile_id, status, count(id) as TOTAL_REJECTED from stream_and_team_subscriber where status = 'REJECTED' group by stream_team_profile_id;
//    CREATE VIEW SUBSCRIPTIONS_REQUESTED_VIEW AS SELECT stream_team_profile_id, status, count(id) as TOTAL_REQUESTED from stre        am_and_team_subscriber where status = 'REQUESTED' group by stream_team_profile_id;

//CREATE VIEW SUBSCRIPTIONS_STATISTIC_VIEW AS SELECT distinct stream_and_team_subscriber.stream_team_profile_id as profile_id, TOTAL_REQUESTED, TOTAL_APPROVED, TOTAL_REJECTED, TOTAL_CANCELED from stream_and_team_subscriber
//    left outer join SUBSCRIPTIONS_APPROVED_VIEW on stream_and_team_subscriber.stream_team_profile_id = SUBSCRIPTIONS_APPROVED_VIEW.stream_team_profile_id
//    left outer join SUBSCRIPTIONS_CANCELED_VIEW on stream_and_team_subscriber.stream_team_profile_id = SUBSCRIPTIONS_CANCELED_VIEW.stream_team_profile_id
//    left outer join SUBSCRIPTIONS_REJECTED_VIEW on stream_and_team_subscriber.stream_team_profile_id = SUBSCRIPTIONS_REJECTED_VIEW.stream_team_profile_id
//    left outer join SUBSCRIPTIONS_REQUESTED_VIEW on stream_and_team_subscriber.stream_team_profile_id = SUBSCRIPTIONS_REQUESTED_VIEW.stream_team_profile_id;

//    CREATE VIEW GROUP_VIEW AS select profile.id as profile_id, course.title, users.name, users.last_name, profile.date_start, profile.date_end, open_for_enroll from profile
//        join course on course.id = profile.course_id
//        join users on users.user_id = profile.user_id
//        where profile.course_id is not null;

//https://stackoverflow.com/questions/53508168/jpa-springboot-repository-for-database-view-not-table

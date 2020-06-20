package by.jrr.profile.service;

import by.jrr.profile.bean.Profile;
import by.jrr.profile.bean.StreamAndTeamSubscriber;
import by.jrr.profile.bean.SubscriptionStatus;
import by.jrr.profile.repository.ProfileRepository;
import by.jrr.profile.repository.StreamAndTeamSubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StreamAndTeamSubscriberService {

    @Autowired
    StreamAndTeamSubscriberRepository streamAndTeamSubscriberRepository;
    @Autowired
    ProfileService profileService;
    @Autowired
    ProfilePossessesService pss;

    public List<StreamAndTeamSubscriber> getAllSubscribersForProfileByProfileId(Long id) {
        return streamAndTeamSubscriberRepository.findAllByStreamTeamProfileId(id);
    }

    public StreamAndTeamSubscriber updateSubscription(Long streamAndTeamProfileId,
                                                      Long subscriberProfileId,
                                                      SubscriptionStatus status) {
        Optional<StreamAndTeamSubscriber> subscriberOptional = streamAndTeamSubscriberRepository
                .findAllByStreamTeamProfileIdAndSubscriberProfileId(streamAndTeamProfileId, subscriberProfileId);
        if(subscriberOptional.isPresent()) {
            return updateSubscriptionWithNewStatus(subscriberOptional, status);
        } else {
            return createSubscriptionAndSetStatus(streamAndTeamProfileId, subscriberProfileId, status);
        }
    }

    public Optional<Profile> findStreamForCourse(Long courseId) {
        return profileService.findNearestFromNowOpennForEnrolStreamByCourseId(courseId);
    }

    private StreamAndTeamSubscriber createSubscriptionAndSetStatus(Long streamAndTeamProfileId,
                                                                   Long subscriberProfileId,
                                                                   SubscriptionStatus status) {
        StreamAndTeamSubscriber subscriber = StreamAndTeamSubscriber.builder()
                .streamTeamProfileId(streamAndTeamProfileId)
                .subscriberProfileId(subscriberProfileId)
                .status(status)
                .build();
        return streamAndTeamSubscriberRepository.save(subscriber);
    }
    private StreamAndTeamSubscriber updateSubscriptionWithNewStatus(Optional<StreamAndTeamSubscriber> subscriberOptional, SubscriptionStatus status) {
        subscriberOptional.get().setStatus(status);
        return streamAndTeamSubscriberRepository.save(subscriberOptional.get());
    }

}

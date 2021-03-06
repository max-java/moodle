package by.jrr.profile.service;

import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.service.UserAccessService;
import by.jrr.auth.service.UserService;
import by.jrr.constant.LinkGenerator;
import by.jrr.email.service.EMailService;
import by.jrr.profile.repository.SubscriptionsStatisticViewDto;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.bean.StreamAndTeamSubscriber;
import by.jrr.profile.bean.SubscriptionStatus;
import by.jrr.profile.repository.StreamAndTeamSubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
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
    @Autowired
    UserService userService;
    @Autowired
    UserAccessService userAccessService;
    @Autowired
    EMailService eMailService;

    public StreamAndTeamSubscriber findSubscribtion(Long userProfileId, Long streamProfileId) throws Exception{
        return streamAndTeamSubscriberRepository.findBySubscriberProfileIdAndStreamTeamProfileId(userProfileId, streamProfileId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<SubscriptionsStatisticViewDto> getSubscriptionsStatistic() {
        List<SubscriptionsStatisticViewDto> result = streamAndTeamSubscriberRepository.getSubscriptionsStatistic();
        return result;
    }

    @Deprecated //to be moved to SubscriptionService
    public List<StreamAndTeamSubscriber> getAllSubscribersForProfileByProfileId(Long id) {
        return streamAndTeamSubscriberRepository.findAllByStreamTeamProfileId(id);
    }

    @Deprecated //to be moved to SubscriptionService
    public List<StreamAndTeamSubscriber> getAllSubscriptionsForProfileByProfileId(Long id) {
        List<StreamAndTeamSubscriber> streamAndTeamSubscriberList = streamAndTeamSubscriberRepository.findAllBySubscriberProfileId(id);
        streamAndTeamSubscriberList.forEach(s -> s.setSubscriptionProfile(profileService.findProfileByProfileIdLazy(s.getStreamTeamProfileId()).orElseGet(Profile::new)));
        return streamAndTeamSubscriberRepository.findAllBySubscriberProfileId(id);
    }

    @Deprecated //to be moved to SubscriptionService
    public boolean isUserSubscribedForProfile(Long streamAndTeamProfileId,
                                              Long subscriberProfileId) {
        Optional<StreamAndTeamSubscriber> subscriberOptional = streamAndTeamSubscriberRepository
                .findAllByStreamTeamProfileIdAndSubscriberProfileId(streamAndTeamProfileId, subscriberProfileId);
        return subscriberOptional.isPresent();
    }

    @Deprecated //use SubscriptionServiceMethods
    public StreamAndTeamSubscriber updateSubscription(Long streamAndTeamProfileId,
                                                      Long subscriberProfileId,
                                                      SubscriptionStatus status) {
        Optional<StreamAndTeamSubscriber> subscriberOptional = streamAndTeamSubscriberRepository
                .findAllByStreamTeamProfileIdAndSubscriberProfileId(streamAndTeamProfileId, subscriberProfileId);
        if (subscriberOptional.isPresent()) {
            return updateSubscriptionWithNewStatus(subscriberOptional, status);
        } else {
            return createSubscriptionAndSetStatus(streamAndTeamProfileId, subscriberProfileId, status);
        }
    }

    public StreamAndTeamSubscriber saveProfileSubscriptionTo(StreamAndTeamSubscriber streamAndTeamSubscriber) {
        Optional<StreamAndTeamSubscriber> subscription = streamAndTeamSubscriberRepository.findAllByStreamTeamProfileIdAndSubscriberProfileId(
                streamAndTeamSubscriber.getStreamTeamProfileId(),
                streamAndTeamSubscriber.getSubscriberProfileId());
        if(subscription.isPresent()) {
            streamAndTeamSubscriber.setId(subscription.get().getId());
        }
        return streamAndTeamSubscriberRepository.save(streamAndTeamSubscriber);
    }

    public StreamAndTeamSubscriber updateProfileSubscriptionTo(StreamAndTeamSubscriber streamAndTeamSubscriber) throws Exception {
        this.findSubscribtion(
                streamAndTeamSubscriber.getSubscriberProfileId(),
                streamAndTeamSubscriber.getStreamTeamProfileId());
        return streamAndTeamSubscriberRepository.save(streamAndTeamSubscriber);
    }

    @Deprecated //use SubscriptionsService Methods
    public void deleteSubscription(Long streamAndTeamProfileId,
                                   Long subscriberProfileId) {
        Optional<StreamAndTeamSubscriber> subscriberOptional = streamAndTeamSubscriberRepository
                .findAllByStreamTeamProfileIdAndSubscriberProfileId(streamAndTeamProfileId, subscriberProfileId);
        if (subscriberOptional.isPresent()) {
            streamAndTeamSubscriberRepository.delete(subscriberOptional.get());
        }
    }

    public void deleteSubscription(StreamAndTeamSubscriber streamAndTeamSubscriber) throws Exception {
        StreamAndTeamSubscriber subscriber = this.findSubscribtion(
                streamAndTeamSubscriber.getSubscriberProfileId(),
                streamAndTeamSubscriber.getStreamTeamProfileId());
        streamAndTeamSubscriberRepository.delete(subscriber);
    }

    public Optional<Profile> findStreamForCourse(Long courseId) {
        return profileService.findNearestFromNowOpennForEnrolStreamByCourseId(courseId);
    }

    private StreamAndTeamSubscriber createSubscriptionAndSetStatus(Long streamAndTeamProfileId,
                                                                   Long subscriberProfileId,
                                                                   SubscriptionStatus status) {
        if(streamAndTeamProfileId.equals(subscriberProfileId)) {
            throw new IllegalArgumentException("streamAndTeamProfileId and subscriberProfileId should not be equal");
        }
            StreamAndTeamSubscriber subscriber = StreamAndTeamSubscriber.builder()
                    .streamTeamProfileId(streamAndTeamProfileId)
                    .subscriberProfileId(subscriberProfileId)
                    .status(status)
                    .build();

        return streamAndTeamSubscriberRepository.save(subscriber);
    }

    private StreamAndTeamSubscriber updateSubscriptionWithNewStatus(Optional<StreamAndTeamSubscriber> subscriberOptional, SubscriptionStatus status) {

        //change role
        try {
            Profile subscriberProfile = profileService.findProfileByProfileId(subscriberOptional.get().getSubscriberProfileId()).get(); // TODO: 23/06/20 I should have entity with all fields populated in this place
            if(!userAccessService.isUserhasRole(UserRoles.ROLE_FREE_STUDENT)){
                //should update role to STUDENT
            }
        } catch (Exception ex) {
            System.out.println(" [ error on attempt to extract userId from subscriber]");
        }
        //send confirmation email
        new Thread(() -> sendConfirmationEmail(subscriberOptional, status)).start();
        //change subscription status
        subscriberOptional.get().setStatus(status);
        return streamAndTeamSubscriberRepository.save(subscriberOptional.get());
    }

    private void sendConfirmationEmail(Optional<StreamAndTeamSubscriber> subscriberOptional, SubscriptionStatus status) {
        // TODO: 23/06/20 add variables to email template and chose email based on subscription status and profile type. (stream or team)
        if (subscriberOptional.isPresent()) {
            if (status.equals(SubscriptionStatus.APPROVED)) {
                try {
                    Profile subscriberProfile = profileService.findProfileByProfileId(subscriberOptional.get().getSubscriberProfileId()).get(); // TODO: 23/06/20 I should have entity with all fields populated in this place
                    Profile streamTeamProfile = profileService.findProfileByProfileId(subscriberOptional.get().getStreamTeamProfileId()).get();

                    String firstAndLastName = subscriberProfile.getUser().getFirstAndLastName();
                    String teamStreamName = streamTeamProfile.getUser().getName() + " " + streamTeamProfile.getUser().getLastName();
                    String streamTeamLink = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + LinkGenerator.getLinkTo(streamTeamProfile); // TODO: 23/06/20 this is a good Class
                    String telegramLink = streamTeamProfile.getTelegramLink();
                    String to = subscriberProfile.getUser().getEmail();

                    if(streamTeamProfile.getUser().getRoles().contains(UserRoles.ROLE_STREAM)) { // TODO: 07/07/20 move all user methods like this to profile
                        eMailService.sendJavaAZSubscriptionConfirmation(firstAndLastName, teamStreamName, streamTeamLink, telegramLink, to); // TODO: 27/07/20 different emmails for different courses
                    }
                    if(streamTeamProfile.getUser().getRoles().contains(UserRoles.ROLE_TEAM)){
                        eMailService.sendTeamSubscriptionConfirmation(firstAndLastName, teamStreamName, streamTeamLink, to);
                    }
                } catch (Exception ex) {
                    System.out.println(" [ error on attempt to extract data to send email confirmation from subscriber]"); // TODO: 07/07/20 continue from here. catch ex and debug

                }
            }
        }
    }

}

package by.jrr.profile.service;

import by.jrr.auth.bean.Role;
import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.service.UserAccessService;
import by.jrr.auth.service.UserService;
import by.jrr.constant.LinkGenerator;
import by.jrr.email.service.EMailService;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.bean.StreamAndTeamSubscriber;
import by.jrr.profile.bean.SubscriptionStatus;
import by.jrr.profile.repository.ProfileRepository;
import by.jrr.profile.repository.StreamAndTeamSubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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


    public List<StreamAndTeamSubscriber> getAllSubscribersForProfileByProfileId(Long id) {
        return streamAndTeamSubscriberRepository.findAllByStreamTeamProfileId(id);
    }
    public List<StreamAndTeamSubscriber> getAllSubscriptionsForProfileByProfileId(Long id) {
        List<StreamAndTeamSubscriber> streamAndTeamSubscriberList = streamAndTeamSubscriberRepository.findAllBySubscriberProfileId(id);
        streamAndTeamSubscriberList.forEach(s -> s.setSubscriptionProfile(profileService.findProfileByProfileIdLazy(s.getStreamTeamProfileId()).get())); // TODO: 24/06/20  get rid of strange .get(), maybe use orElseGet, but what else to get?
        return streamAndTeamSubscriberRepository.findAllBySubscriberProfileId(id);
    }
    public boolean isUserSubscribedForProfile(Long streamAndTeamProfileId,
                                              Long subscriberProfileId) {
        Optional<StreamAndTeamSubscriber> subscriberOptional = streamAndTeamSubscriberRepository
                .findAllByStreamTeamProfileIdAndSubscriberProfileId(streamAndTeamProfileId, subscriberProfileId);
        return subscriberOptional.isPresent();
    }

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
    public void deleteSubscription(Long streamAndTeamProfileId,
                                   Long subscriberProfileId) {
        Optional<StreamAndTeamSubscriber> subscriberOptional = streamAndTeamSubscriberRepository
                .findAllByStreamTeamProfileIdAndSubscriberProfileId(streamAndTeamProfileId, subscriberProfileId);
        if (subscriberOptional.isPresent()) {
            streamAndTeamSubscriberRepository.delete(subscriberOptional.get());
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

        //change role
        try {
            Profile subscriberProfile = profileService.findProfileByProfileId(subscriberOptional.get().getSubscriberProfileId()).get(); // TODO: 23/06/20 I should have entity with all fields populated in this place
            if(!userAccessService.isUserhasRole(UserRoles.ROLE_FREE_STUDENT)){
                userService.addRoleToUser(UserRoles.ROLE_FREE_STUDENT, subscriberProfile.getUserId()); // TODO: 25/06/20 should role be changed here like this?
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
                        eMailService.sendStreamSubscriptionConfirmation(firstAndLastName, teamStreamName, streamTeamLink, telegramLink, to);
                    }
                    if(streamTeamProfile.getUser().getRoles().contains(UserRoles.ROLE_TEAM)){
                        eMailService.sendTeamSubscriptionConfirmation(firstAndLastName, teamStreamName, streamTeamLink, to);
                    }
                } catch (Exception ex) {
                    System.out.println(" [ error on attempt to extract data to send email confirmation from subscriber]");
                }
            }
        }
    }

}

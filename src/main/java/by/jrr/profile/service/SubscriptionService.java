package by.jrr.profile.service;

import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.service.UserAccessService;
import by.jrr.auth.service.UserRoleManager;
import by.jrr.constant.LinkGenerator;
import by.jrr.crm.bean.NoteItem;
import by.jrr.crm.common.HistoryType;
import by.jrr.crm.service.HistoryItemService;
import by.jrr.email.service.EMailService;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.bean.StreamAndTeamSubscriber;
import by.jrr.profile.bean.SubscriptionStatus;
import by.jrr.profile.mapper.SubscriptionMapper;
import by.jrr.profile.model.SubscriptionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class SubscriptionService {

    @Autowired
    HistoryItemService historyItemService;
    @Autowired
    StreamAndTeamSubscriberService streamAndTeamSubscriberService;
    @Autowired
    ProfileService profileService;
    @Autowired
    UserRoleManager userRoleManager;
    @Autowired
    EMailService eMailService;
    @Autowired
    UserAccessService userAccessService;

    public SubscriptionDto.Response requestSubscription(SubscriptionDto.Request subscReq) {
        SubscriptionDto.Response response = new SubscriptionDto.Response();
        if (!isProfileIdSubscribedTo(subscReq.getSubscriberProfileId(), subscReq.getStreamTeamProfileId())) {
            StreamAndTeamSubscriber subscriber = SubscriptionMapper.OF.getStreamTeamSubscriberFrom(subscReq);
            subscriber.setStatus(SubscriptionStatus.REQUESTED);
            streamAndTeamSubscriberService.saveProfileSubscriptionTo(subscriber);

            String responseNotes = this.saveNoteItemForProfile("Subscription request for %s", subscReq);
            response.setNotes(responseNotes);
            //todo: send notification
        }
        response.setNotes("Subscription exists");
        return response;
    }

    public SubscriptionDto.Response approveSubscription(SubscriptionDto.Request subscReq) {
        //considering status has not been set on UI.
        SubscriptionDto.Response response = new SubscriptionDto.Response();

        StreamAndTeamSubscriber subscriber = SubscriptionMapper.OF.getStreamTeamSubscriberFrom(subscReq);
        subscriber.setStatus(SubscriptionStatus.APPROVED);

        streamAndTeamSubscriberService.saveProfileSubscriptionTo(subscriber);

        String responseNotes = this.saveNoteItemForProfile("Subscription approved for %s ", subscReq);
        response.setNotes(responseNotes);

        upgradeUserRole(subscReq);

        sendConfirmationEmail(subscReq);


        return response;
    }

    public SubscriptionDto.Response rejectSubscription(SubscriptionDto.Request subscReq) {
        SubscriptionDto.Response response = new SubscriptionDto.Response();
        StreamAndTeamSubscriber subscriber = SubscriptionMapper.OF.getStreamTeamSubscriberFrom(subscReq);
        try {
            streamAndTeamSubscriberService.deleteSubscription(subscriber);
            String responseNotes = this.saveNoteItemForProfile("Subscription deleted by admin for %s ", subscReq);
            response.setNotes(responseNotes);
            //todo: send notification
        } catch (Exception ex) {
            response.setNotes("Subscription doesn't exist");
        }
        return response;
    }

    public SubscriptionDto.Response unsubscribe(SubscriptionDto.Request subscReq) {
        SubscriptionDto.Response response = new SubscriptionDto.Response();
        StreamAndTeamSubscriber subscriber = SubscriptionMapper.OF.getStreamTeamSubscriberFrom(subscReq);
        try {
            streamAndTeamSubscriberService.deleteSubscription(subscriber);
            String responseNotes = this.saveNoteItemForProfile("Subscription canceled by user for %s ", subscReq);
            response.setNotes(responseNotes);
            //todo: send notification
        } catch (Exception ex) {
            response.setNotes("Subscription doesn't exist");
        }
        return response;

    }

    private boolean isProfileIdSubscribedTo(Long userProfileId, Long streamProfileId) {
        try {
            streamAndTeamSubscriberService.findSubscribtion(userProfileId, streamProfileId);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * returns String note for popUp user notification
     */
    private String saveNoteItemForProfile(String message, SubscriptionDto.Request subscReq) {
        if (userAccessService.isCurrentUserIsAdmin()) {

        }
        String streamName = profileService.findStreamNameByStreamProfileId(subscReq.getStreamTeamProfileId());
        String noteText = String.format(message, streamName);

        NoteItem noteItem = new NoteItem();
        noteItem.setProfileId(subscReq.getSubscriberProfileId());
        noteItem.setText(noteText);
        setNoteType(subscReq, noteItem);
        historyItemService.saveNoteForProfile(noteItem);
        return noteText;
    }

    private void upgradeUserRole(SubscriptionDto.Request subscReq) {
        if (profileService.isStreamFree(subscReq.getStreamTeamProfileId())) {
            profileService.addRoleIfAbsent(subscReq.getSubscriberProfileId(), UserRoles.ROLE_FREE_STUDENT);
        } else {
            profileService.addRoleIfAbsent(subscReq.getSubscriberProfileId(), UserRoles.ROLE_STUDENT);
        }
    }

    private void sendConfirmationEmail(SubscriptionDto.Request subscReq) {
        try {
            Profile subscriberProfile = profileService.findProfileByProfileIdLazyWithUserData(subscReq.getSubscriberProfileId());
            Profile streamTeamProfile = profileService.findProfileByProfileIdLazyWithUserData(subscReq.getStreamTeamProfileId());

            String firstAndLastName = subscriberProfile.getUser().getFirstAndLastName();
            String streamName = String.format("%s : %s",
                    streamTeamProfile.getUser().getName(),
                    streamTeamProfile.getUser().getLastName());
            String streamTeamLink = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + LinkGenerator.getLinkTo(streamTeamProfile);
            String telegramLink = streamTeamProfile.getTelegramLink();
            String to = subscriberProfile.getUser().getEmail();

            eMailService.sendStreamSubscriptionConfirmation(firstAndLastName, streamName, streamTeamLink, telegramLink, to);
        } catch (Exception ex) {
            System.out.println("[SubscriptionService.sendConfirmationEmail] : error extracting data & sending email for" + subscReq.toString());

        }
    }

    private void setNoteType(SubscriptionDto.Request subscReq, NoteItem noteItem) {
        if (userAccessService.isCurrentUserIsAdmin()) {
            if (profileService.getCurrentUserProfileId() != subscReq.getSubscriberProfileId()) {
                noteItem.setType(HistoryType.NOTE);
            }
        } else {
            noteItem.setType(HistoryType.USER_ACTION);
        }
    }
}

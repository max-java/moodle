package by.jrr.profile.service;

import by.jrr.crm.bean.NoteItem;
import by.jrr.crm.common.HistoryType;
import by.jrr.crm.service.HistoryItemService;
import by.jrr.profile.bean.StreamAndTeamSubscriber;
import by.jrr.profile.bean.SubscriptionStatus;
import by.jrr.profile.mapper.SubscriptionMapper;
import by.jrr.profile.model.SubscriptionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    @Autowired
    HistoryItemService historyItemService;
    @Autowired
    StreamAndTeamSubscriberService streamAndTeamSubscriberService;
    @Autowired
    ProfileService profileService;

    public SubscriptionDto.Response requestSubscription(SubscriptionDto.Request subscReq) {
        SubscriptionDto.Response response = new SubscriptionDto.Response();
        if (!isProfileIdSubscribedTo(subscReq.getSubscriberProfileId(), subscReq.getStreamTeamProfileId())) {
            StreamAndTeamSubscriber subscriber = SubscriptionMapper.OF.getStreamTeamSubscriberFrom(subscReq);
            subscriber.setStatus(SubscriptionStatus.REQUESTED);
            streamAndTeamSubscriberService.saveProfileSubscriptionTo(subscriber);

            String responseNotes = this.saveNoteItemForProfile("Subscription request for %s", subscReq, HistoryType.USER_ACTION);
            response.setNotes(responseNotes);
            //todo: send notification
        }
        response.setNotes("Subscription exists");
        return response;
    }

    public SubscriptionDto.Response approveSubscription(SubscriptionDto.Request subscReq) {
        //considering status has been set.
        SubscriptionDto.Response response = new SubscriptionDto.Response();
        StreamAndTeamSubscriber subscriber = SubscriptionMapper.OF.getStreamTeamSubscriberFrom(subscReq);
        subscriber.setStatus(SubscriptionStatus.APPROVED);
        streamAndTeamSubscriberService.saveProfileSubscriptionTo(subscriber);
        String responseNotes = this.saveNoteItemForProfile("Subscription approved for %s ", subscReq, HistoryType.NOTE);
        response.setNotes(responseNotes);
        //todo: send notification
        //todo: change user Role

        response.setNotes("Subscription exists");
        return response;
    }

    public SubscriptionDto.Response rejectSubscription(SubscriptionDto.Request subscReq) {
        SubscriptionDto.Response response = new SubscriptionDto.Response();
        StreamAndTeamSubscriber subscriber = SubscriptionMapper.OF.getStreamTeamSubscriberFrom(subscReq);
        try {
            streamAndTeamSubscriberService.deleteSubscription(subscriber);
            String responseNotes = this.saveNoteItemForProfile("Subscription deleted by admin for %s ", subscReq, HistoryType.NOTE);
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
            String responseNotes = this.saveNoteItemForProfile("User canceled subscription for %s ", subscReq, HistoryType.USER_ACTION);
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
     * */
    private String saveNoteItemForProfile(String message, SubscriptionDto.Request subscReq, HistoryType type) {
        String StreamName = profileService.findStreamNameByStreamProfileId(subscReq.getStreamTeamProfileId());
        String noteText = String.format(message, StreamName);

        NoteItem noteItem = new NoteItem();
        noteItem.setProfileId(subscReq.getSubscriberProfileId());
        noteItem.setText(noteText);
        noteItem.setType(type);
        historyItemService.saveNoteForProfile(noteItem);
        return noteText;
    }
}

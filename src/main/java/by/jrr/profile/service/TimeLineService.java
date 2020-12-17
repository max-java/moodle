package by.jrr.profile.service;

import by.jrr.auth.service.UserAccessService;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.bean.StreamAndTeamSubscriber;
import by.jrr.profile.bean.TimeLine;
import by.jrr.profile.repository.TimeLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TimeLineService {

    @Autowired
    TimeLineRepository timeLineRepository;
    @Autowired
    UserAccessService userAccessService;

    public void save(TimeLine timeLine) {
        timeLineRepository.save(timeLine);
    }

    public void delete(TimeLine timeLine) {
        timeLineRepository.delete(timeLine);
    }

    public void delete(Long id) {
        timeLineRepository.deleteById(id);
    }

    public List<TimeLine> getTimelineByStreamId(Long streamTeamProfileId) {
        List<TimeLine> timeline = timeLineRepository.findAllByStreamTeamProfileId(streamTeamProfileId);
        if(!userAccessService.isUserHasAccessToSubcription(streamTeamProfileId)) {
            closeLinksInTimeline(timeline);
        }
        return timeline;
    }

    //protect access for unsubscribed users
    private void closeLinksInTimeline(List<TimeLine> timeline) {
        for (TimeLine item : timeline ) {
            switch (item.getEventType()) {
                case YOUTUBE: item.setUrlToRedirect("https://www.youtube.com/embed/0BJOOo4Sa7M"); break;
                case VIDEO: item.setUrlToRedirect("common/403_access_denied.mp4"); break;
                case TELEGRAM_CHAT: break;
                default: item.setUrlToRedirect("/403"); break;
            }
        }
    }

    public List<TimeLine> getTimelineForProfileSubscriptions(List<StreamAndTeamSubscriber> subscriptions) {
        List<TimeLine> timeLines = new ArrayList<>();
        for(StreamAndTeamSubscriber subscriber : subscriptions) {
            timeLines.addAll(this.getTimelineByStreamId(subscriber.getStreamTeamProfileId()));
        }
        return timeLines;
    }
    public void distinctTimelineItems(List<TimeLine> timeline) { // TODO: 21/09/20 not working. Create tests.
        List<TimeLine> itemsToRemove = new ArrayList<>();
        for (TimeLine item : timeline) {
            int i = 0;
            for (TimeLine item2 : timeline) {
                if (item.getUrlToRedirect().equals(item2.getUrlToRedirect())) {
                    i++;
                    if (i > 1) {
                        itemsToRemove.add(item2);
                    }
                }
            }
        }
        itemsToRemove.removeAll(itemsToRemove);
    }

    public Map<LocalDate, List<TimeLine>> groupTimelineByDates(List<TimeLine> timeLineList) {
        Map<LocalDate, List<TimeLine>> result = timeLineList.stream()
                .filter(timeLine -> timeLine.getDateTime() != null) // TODO: 07/08/20 add default value
                .collect(Collectors.groupingBy(dt -> dt.getDateTime().toLocalDate(), Collectors.toList()));
        Map<LocalDate, List<TimeLine>> sortedResultReversed = new TreeMap<>(Collections.reverseOrder());
        sortedResultReversed.putAll(result);
        return sortedResultReversed;
    }
    public Map<LocalDate, List<TimeLine>> getTimelineForProfile(Profile profile) {
        List<TimeLine> timeline = new ArrayList<>();
        timeline.addAll(getTimelineByStreamId(profile.getId()));
        timeline.addAll(getTimelineForProfileSubscriptions(profile.getSubscriptions()));
        distinctTimelineItems(timeline);
        return groupTimelineByDates(timeline);
    }


}

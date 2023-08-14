package by.jrr.statistic.service;

import by.jrr.feedback.bean.EntityType;
import by.jrr.interview.bean.QAndA;
import by.jrr.interview.service.QAndAService;
import by.jrr.moodle.bean.Lecture;
import by.jrr.moodle.bean.Topic;
import by.jrr.moodle.service.LectureService;
import by.jrr.moodle.service.TopicService;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfileService;
import by.jrr.statistic.bean.TrackStatus;
import by.jrr.statistic.bean.Trackable;
import by.jrr.statistic.bean.UserProgress;
import by.jrr.statistic.repository.UserProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProgressService {

    @Autowired
    UserProgressRepository userProgressRepository;
    @Autowired
    ProfileService profileService; // TODO: 30/05/20 cash values that used

    @Autowired
    QAndAService qAndAService;
    @Autowired
    LectureService lectureService;
    @Autowired
    TopicService topicService;

    public void saveProgress(Trackable item,
                             TrackStatus trackStatus) {
        try {
            Optional<UserProgress> up = userProgressRepository.findByTrackableIdAndProfileId(item.getId(), profileService.getCurrentUserProfile().getId());
            if (up.isPresent()) {
                updateProgress(item, profileService.getCurrentUserProfile().getId(), trackStatus, up.get());

            } else {
                createAndSaveProgress(item, profileService.getCurrentUserProfile().getId(), trackStatus);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public TrackStatus getUserProfileForTrackable(Trackable item) { // TODO: 05/06/20 should be named getStatus for Trackable for current user?
        try {
            Optional<UserProgress> up = userProgressRepository          // TODO: 05/06/20 What this method does?
                    .findByTrackableIdAndProfileId(item.getId(), profileService.getCurrentUserProfile().getId());
            if (up.isPresent()) {
                return up.get().getTrackStatus();

            } else {
                return TrackStatus.NONE;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return TrackStatus.NONE;
    }


// // // // TODO: 05/06/20 consider to move this in separate service
//
    public List<UserProgress> getAllUserProgressByProfileIdLazy(Long profileId) {
        List<UserProgress> userProgressList = userProgressRepository.findByProfileId(profileId);
        return userProgressList;
    }

    public List<UserProgress> getAllUserProgressByProfileIdAndEntityTypeLazy(Long profileId, EntityType entityType) {
        List<UserProgress> userProgressList = userProgressRepository.findByProfileIdAndTrackableType(profileId, entityType);
        userProgressList.stream().forEach(up -> setTrackableItemToUserProgress(up));
        return userProgressList;
    }
    public List<UserProgress> getAllUserProgressByProfileIdEager(Long profileId) {
        List<UserProgress> userProgressList = userProgressRepository.findByProfileId(profileId);
        userProgressList.stream().forEach(up -> setTrackableItemToUserProgress(up));
        return userProgressList;
    }

    public List<UserProgress> getAllUserProgressByProfileIdAndEntityTypeEager(Long profileId, EntityType entityType) {
        List<UserProgress> userProgressList = userProgressRepository.findByProfileIdAndTrackableType(profileId, entityType);
        return userProgressList;
    }
    private void setTrackableItemToUserProgress(UserProgress userProgress) {
        switch (userProgress.getTrackableType()) {
            case INTERVIEW_QUESTION: // TODO: 05/06/20 consider to change ofElseGet to ofElseThrow, because in this case null is impossible and should be treated as an error
                userProgress.setTrackable(qAndAService.findById(userProgress.getTrackableId()).orElseGet(QAndA::new));
                break;
            case LECTURE:
                userProgress.setTrackable(lectureService.findById(userProgress.getTrackableId()).orElseGet(Lecture::new));
                break;
            case TOPIC:
                userProgress.setTrackable(topicService.findById(userProgress.getTrackableId()).orElseGet(Topic::new));
                break;
        }
    }
//
// // //  // TODO: 05/06/20 because it is like separate getTrackabeStatus for user Service operations

    private void createAndSaveProgress(Trackable item,
                                       Long profileId,
                                       TrackStatus trackStatus) {
        UserProgress userProgress = UserProgress.builder()
                .profileId(profileId)
                .trackableId(item.getId())
                .trackableType(item.getType())
                .trackableName(item.getName())
                .trackableLink("") // TODO: 29/05/20 set link to trackabe and reviewable. Update reviewable(reviewRequest?) in the same way
                .trackStatus(trackStatus)
                .build();
        // TODO: 29/05/20 set date created
        userProgressRepository.save(userProgress);

    }

    private void updateProgress(Trackable item,
                                Long profileId,
                                TrackStatus trackStatus,
                                UserProgress userProgress) {
        userProgress.setTrackStatus(trackStatus);
        userProgressRepository.save(userProgress);
        // TODO: 29/05/20 date updated
    }
}

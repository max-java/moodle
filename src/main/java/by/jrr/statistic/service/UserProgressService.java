package by.jrr.statistic.service;

import by.jrr.profile.service.ProfileService;
import by.jrr.statistic.bean.TrackStatus;
import by.jrr.statistic.bean.Trackable;
import by.jrr.statistic.bean.UserProgress;
import by.jrr.statistic.repository.UserProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProgressService {

    @Autowired
    UserProgressRepository userProgressRepository;
    @Autowired
    ProfileService profileService; // TODO: 30/05/20 cash values that used

    public void saveProgress(Trackable item,
                             TrackStatus trackStatus) {
        Optional<UserProgress> up = userProgressRepository.findByTrackableIdAndProfileId(item.getId(), profileService.getCurrentUserProfile().getId());
        if(up.isPresent()) {
            updateProgress(item, profileService.getCurrentUserProfile().getId(), trackStatus, up.get());

        } else {
            createAndSaveProgress(item, profileService.getCurrentUserProfile().getId(), trackStatus);
        }
    }
    public TrackStatus getUserProfileForTrackable(Trackable item) {
        Optional<UserProgress> up = userProgressRepository.findByTrackableIdAndProfileId(item.getId(), profileService.getCurrentUserProfile().getId());
        if(up.isPresent()) {
            return up.get().getTrackStatus();

        } else {
            return TrackStatus.NONE;
        }
    }

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

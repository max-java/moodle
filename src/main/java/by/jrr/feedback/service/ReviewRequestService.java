package by.jrr.feedback.service;

import by.jrr.feedback.bean.Item;
import by.jrr.feedback.bean.ReviewRequest;
import by.jrr.feedback.bean.Reviewable;
import by.jrr.feedback.repository.ReviewRequestRepository;
import by.jrr.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewRequestService {

    @Autowired
    ReviewRequestRepository reviewRequestRepository;
    @Autowired
    ProfileService profileService;

    public ReviewRequest createNewReviewRequest(Item item, Reviewable reviewable) {
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setItemId(item.getId());
        reviewRequest.setReviewedEntityId(reviewable.getId());
        reviewRequest.setRequesterProfileId(profileService.getCurrentUserProfile().getId());
        return reviewRequestRepository.save(reviewRequest);
    }

    public ReviewRequest saveOrUpdateReviewRequest(ReviewRequest reviewRequest) {
        // only reviewer notes and link to request could be updated,
        // because other fields from save form data endpoint comes empty
        Optional<ReviewRequest> savedReviewRequest = reviewRequestRepository.findById(reviewRequest.getId());
        if (savedReviewRequest.isPresent()) {
            ReviewRequest updatedRequest = savedReviewRequest.get();
            updatedRequest.setRequesterNotes(reviewRequest.getRequesterNotes());
            updatedRequest.setLink(reviewRequest.getLink());
            return reviewRequestRepository.save(updatedRequest);

        }
        return null; // TODO: 28/05/20 return Optional and handle it with logger in controller

    }
}

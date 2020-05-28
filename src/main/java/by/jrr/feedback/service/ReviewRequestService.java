package by.jrr.feedback.service;

import by.jrr.feedback.bean.Item;
import by.jrr.feedback.bean.ReviewRequest;
import by.jrr.feedback.bean.Reviewable;
import by.jrr.feedback.repository.ReviewRequestRepository;
import by.jrr.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        reviewRequest.setCreatedDate(LocalDateTime.now());
        return reviewRequestRepository.save(reviewRequest);
    }

    public ReviewRequest updateMessageAndLinkOnReviewRequest(ReviewRequest reviewRequest) {
        // only reviewer notes and link to request could be updated,
        // because other fields from save form data endpoint comes empty (todo is it true and whY? Is it fixable and should it be? Otherwise could be secure? could user modify fields in POST and save new IDs?
        Optional<ReviewRequest> savedReviewRequest = reviewRequestRepository.findById(reviewRequest.getId());
        if (savedReviewRequest.isPresent()) {
            ReviewRequest updatedRequest = savedReviewRequest.get();
            updatedRequest.setRequesterNotes(reviewRequest.getRequesterNotes());
            updatedRequest.setLink(reviewRequest.getLink());
            return reviewRequestRepository.save(updatedRequest);

        }
        return null; // TODO: 28/05/20 return Optional and handle it with logger in controller
    }

    public ReviewRequest closeReviewRequest(ReviewRequest reviewRequest) {
        // only status on close and closed date could be updated
        // because other fields from save form data endpoint comes empty
        Optional<ReviewRequest> savedReviewRequest = reviewRequestRepository.findById(reviewRequest.getId());
        if (savedReviewRequest.isPresent()) {
            ReviewRequest updatedRequest = savedReviewRequest.get();
            updatedRequest.setReviewResultOnClosing(reviewRequest.getReviewResultOnClosing());
            updatedRequest.setClosedDate(LocalDateTime.now());
            return reviewRequestRepository.save(updatedRequest);

        }
        return null; // TODO: 28/05/20 return Optional and handle it with logger in controller
    }
}

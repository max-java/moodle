package by.jrr.feedback.service;

import by.jrr.feedback.bean.Item;
import by.jrr.feedback.bean.Review;
import by.jrr.feedback.bean.ReviewRequest;
import by.jrr.feedback.bean.Reviewable;
import by.jrr.feedback.repository.ReviewRequestRepository;
import by.jrr.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Facade for all services
 */

@Service
public class FeedbackService {

    @Autowired
    ItemService itemService;
    @Autowired
    ReviewRequestRepository reviewRequestRepository;
    @Autowired
    ProfileService profileService;
    @Autowired
    ReviewRequestService reviewRequestService;
    @Autowired
    ReviewService reviewService;

    public ReviewRequest createNewReviewRequest(Reviewable reviewable) {
        Item item = itemService.getItemByReviewable(reviewable);
        return reviewRequestService.createNewReviewRequest(item, reviewable);
    }

    public Optional<ReviewRequest> getReviewRequestById(Long id) {
        Optional<ReviewRequest> reviewRequest = reviewRequestRepository.findById(id); // TODO: 28/05/20 move to reviewRequestService!!
        if (reviewRequest.isPresent()) {
            ReviewRequest revReq = reviewRequest.get();
            revReq.setReviews(reviewService.findAllByReviewRequestId(id));
            return Optional.of(revReq);
        }
        return reviewRequestRepository.findById(id);
    }

    public Item getItemByReviewRequest(ReviewRequest reviewRequest) {
        return itemService.getItemByReviewRequest(reviewRequest);
    }

    public ReviewRequest saveOrUpdateReviewRequest(ReviewRequest reviewRequest) {
        return reviewRequestService.saveOrUpdateReviewRequest(reviewRequest);
    }

    public void saveReview(Review review) {
        reviewService.save(review);
    }
}

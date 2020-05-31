package by.jrr.feedback.service;

import by.jrr.auth.bean.User;
import by.jrr.feedback.bean.Item;
import by.jrr.feedback.bean.Review;
import by.jrr.feedback.bean.ReviewRequest;
import by.jrr.feedback.bean.Reviewable;
import by.jrr.feedback.repository.ReviewRequestRepository;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
    ReviewRequestPageableSearchService reviewRequestPageableSearchService;
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

    public ReviewRequest updateMessageAndLinkOnReviewRequest(ReviewRequest reviewRequest) {
        return reviewRequestService.updateMessageAndLinkOnReviewRequest(reviewRequest);
    }
    public ReviewRequest closeReviewRequest(ReviewRequest reviewRequest) {
        return reviewRequestService.closeReviewRequest(reviewRequest);
    }

    public void saveReview(Review review) {
        reviewService.save(review);
    }

    public Page<ReviewRequest> findAllReviewRequestPageable(Optional<Integer> page, Optional<Integer> elem, Optional<String> searchTerm) {
        return reviewRequestPageableSearchService.findAllReviewRequestPageable(page, elem, searchTerm);


    }
}

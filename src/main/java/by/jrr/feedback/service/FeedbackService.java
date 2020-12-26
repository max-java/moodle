package by.jrr.feedback.service;

import by.jrr.common.annotations.Wip;
import by.jrr.feedback.bean.Item;
import by.jrr.feedback.bean.Review;
import by.jrr.feedback.bean.ReviewRequest;
import by.jrr.feedback.bean.Reviewable;
import by.jrr.feedback.elements.RequestForReviewDto;
import by.jrr.feedback.repository.ReviewRequestRepository;
import by.jrr.profile.service.ProfilePossessesService;
import by.jrr.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import by.jrr.feedback.mappers.RequestForReviewDtoMapper;

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
    ProfilePossessesService pss;

    public ReviewRequest createNewRequestForReview(final RequestForReviewDto requestForReviewDto) {
        final Reviewable reviewable = RequestForReviewDtoMapper.OF.requestForReviewDtoToReviewable(requestForReviewDto);
        final Item item = itemService.getOrCreateItem(reviewable);
        requestForReviewDto.setItemId(item.getId());

        final ReviewRequest rr = RequestForReviewDtoMapper.OF.requestForReviewDtoToReviewRequest(requestForReviewDto);
        return reviewRequestService.createRequestForReview(rr);
    }

    public ReviewRequest updateRequestForReview(final RequestForReviewDto requestForReviewDto) {
        return reviewRequestService.updateMessageAndLinkOnReviewRequest(
                requestForReviewDto.getId(),
                requestForReviewDto.getRequesterNotes(),
                requestForReviewDto.getLink());
    }

    @Deprecated//(since = "2020-12-26: use createNewRequestForReview", forRemoval = true)
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

    @Deprecated//(since = "2020-12-26: use updateRequestForReview", forRemoval = true)
    public ReviewRequest updateMessageAndLinkOnReviewRequest(ReviewRequest reviewRequest) {
        return reviewRequestService.updateMessageAndLinkOnReviewRequest(
                reviewRequest.getId(),
                reviewRequest.getRequesterNotes(),
                reviewRequest.getLink());
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

    public List<ReviewRequest> fingAllReviewRequestForUser(Long profileId) {
        List<ReviewRequest> reviewRequestList = reviewRequestService.findReviewRequestForUser(profileId);


        return reviewRequestService.findReviewRequestForUser(profileId);
    }

    public void deleteRequestForReview(Long id) {
        reviewRequestService.deleteRequestForReviewById(id);
    }
}

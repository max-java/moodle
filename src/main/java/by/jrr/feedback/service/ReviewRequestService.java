package by.jrr.feedback.service;

import by.jrr.auth.service.UserAccessService;
import by.jrr.common.annotations.ToDeprecated;
import by.jrr.feedback.bean.*;
import by.jrr.feedback.repository.ReviewRequestRepository;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfilePossessesService;
import by.jrr.profile.service.ProfileService;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewRequestService {

    @Autowired
    ReviewRequestRepository reviewRequestRepository;
    @Autowired
    ProfileService profileService;
    @Autowired
    ReviewService reviewService;
    @Autowired
    ItemService itemService;
    @Autowired
    ProfilePossessesService pss;
    @Autowired
    UserAccessService userAccessService;

    @ToDeprecated("use createRequestForReview()")
    public ReviewRequest createNewReviewRequest(Item item, Reviewable reviewable) {
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setItemId(item.getId());
        reviewRequest.setReviewedEntityId(reviewable.getId());
        reviewRequest.setRequesterProfileId(profileService.getCurrentUserProfileId());
        reviewRequest.setCreatedDate(LocalDateTime.now());
        reviewRequest.setReviewResultOnClosing(ReviewResult.NONE);
        ReviewRequest rr = reviewRequestRepository.save(reviewRequest);
        pss.savePossessForCurrentUser(rr.getId(), EntityType.REVIEW_REQUEST);
        return reviewRequestRepository.save(reviewRequest);
    }

    public ReviewRequest createRequestForReview(ReviewRequest rr) {
        setInitialValues(rr);
        rr = reviewRequestRepository.save(rr);
        pss.savePossessForCurrentUser(rr.getId(), EntityType.REVIEW_REQUEST);
        return rr;
    }

    private void setInitialValues(ReviewRequest rr) {
        rr.setRequesterProfileId(profileService.getCurrentUserProfileId());
        rr.setCreatedDate(LocalDateTime.now());
        rr.setReviewResultOnClosing(ReviewResult.NONE);
    }

    /***
     * Most of ReviewRequest values are not changed during life cycle. Only three fields considering changeable:
     * - requester message
     * - link to reviewed item
     * - review result
     * That's way it's redundant to change ReviewRequest all at once, but repository methods should be changed to
     * update only 2 fields. Here:
     *     private String requesterNotes;
     *     private String link;
     * @param reviewId
     * @param requesterNotes
     * @param link
     * @return ReviewRequest
     *
     */
    public ReviewRequest updateMessageAndLinkOnReviewRequest(Long reviewId, String requesterNotes, String link) {

        Optional<ReviewRequest> savedReviewRequest = reviewRequestRepository.findById(reviewId);
        if (savedReviewRequest.isPresent()) {
            if(userAccessService.isCurrentUserIsAdmin()
                    || pss.isUserOwner(profileService.getCurrentUserProfileId(), reviewId)) { //todo: move this clause to isUserCanUpdateRequestForReview
                ReviewRequest updatedRequest = savedReviewRequest.get();
                updatedRequest.setRequesterNotes(requesterNotes);
                updatedRequest.setLink(link);
                return reviewRequestRepository.save(updatedRequest);
            }
        }
        return null; // TODO: 26/12/20 should throw EntityNotFoundException if not present?
    }


    /***
     * Most of ReviewRequest values are not changed during life cycle. Only three fields considering changeable:
     * - requester message
     * - link to reviewed item
     * - review result
     * That's way it's redundant to change ReviewRequest all at once, but repository methods should be changed to
     * update only 2 fields. Here:
     *     private LocalDateTime closedDate;
     *     private ReviewResult reviewResultOnClosing;
     * @param reviewRequest
     * @return
     */
    public ReviewRequest closeReviewRequest(ReviewRequest reviewRequest) {
        Optional<ReviewRequest> savedReviewRequest = reviewRequestRepository.findById(reviewRequest.getId());
        if (savedReviewRequest.isPresent() && userAccessService.isCurrentUserIsAdmin()) {
            ReviewRequest updatedRequest = savedReviewRequest.get();
            updatedRequest.setReviewResultOnClosing(reviewRequest.getReviewResultOnClosing());
            updatedRequest.setClosedDate(LocalDateTime.now());
            return reviewRequestRepository.save(updatedRequest);

        }
        return null; // TODO: 26/12/20 should throw EntityNotFoundException if not present?
    }

    public List<ReviewRequest> findReviewRequestForUser(Long profileId) {
        List<ReviewRequest> reviewRequestList = reviewRequestRepository.findAllByRequesterProfileId(profileId);
        reviewRequestList.stream()
                .map(this::setRequesterProfileToReviewRequest)
                .map(this::setReviewsToReviewRequest)
                .map(this::setItemsToReviewRequest)
                .map(this::setReviewedEntityToItemInReviewRequest)
                .collect(Collectors.toList());
        return reviewRequestList;
    }

    public List<ReviewRequest> findAllRequestsForReviewByItemId(Long itemId) {
        List<ReviewRequest> reviewRequestList = reviewRequestRepository.findAllByItemId(itemId);
        reviewRequestList.stream()
                .map(this::setRequesterProfileToReviewRequest)
                .map(this::setReviewsToReviewRequest)
                .map(this::setItemsToReviewRequest)
                .map(this::setReviewedEntityToItemInReviewRequest)
                .collect(Collectors.toList());
        return reviewRequestList;
    }

    public ReviewRequest setReviewsToReviewRequest(ReviewRequest rr) {
        rr.setReviews(reviewService.findAllByReviewRequestId(rr.getId()));
        return rr;
    }
    private ReviewRequest setItemsToReviewRequest(ReviewRequest rr) {
        rr.setItem(itemService.getItemByReviewRequest(rr));
        return rr;
    }
    private ReviewRequest setRequesterProfileToReviewRequest(ReviewRequest rr) {
        rr.setRequesterProfile(profileService.findProfileByProfileId(rr.getRequesterProfileId()).orElseGet(Profile::new));
        return rr;
    }
    private ReviewRequest setReviewedEntityToItemInReviewRequest(ReviewRequest rr) {
        rr.getItem()
            .setReviewedEntity(itemService.getReviewableByReviewableId(rr.getReviewedEntityId()));
        return rr;
    }

    public void deleteRequestForReviewById(Long id){
        if(reviewRequestRepository.existsById(id)) {
            ReviewRequest rr = reviewRequestRepository.findById(id).get();
            setReviewsToReviewRequest(rr);
            reviewService.deleteReviews(rr.getReviews());
            reviewRequestRepository.deleteById(id);
            //todo: delete pss.
        }
    }
}

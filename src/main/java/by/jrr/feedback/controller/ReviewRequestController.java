package by.jrr.feedback.controller;

import by.jrr.auth.service.UserAccessService;
import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.LinkGenerator;
import by.jrr.constant.View;
import by.jrr.feedback.bean.Item;
import by.jrr.feedback.bean.Review;
import by.jrr.feedback.bean.ReviewRequest;
import by.jrr.feedback.bean.ReviewResult;
import by.jrr.feedback.service.FeedbackService;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class ReviewRequestController {

    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    FeedbackService feedbackService;
    @Autowired
    ProfileService profileService;
    @Autowired
    UserAccessService userAccessService;


    @GetMapping(Endpoint.REVIEW_REQUEST_FORM + "/{id}")
    public ModelAndView createNewReviewRequest(@PathVariable Long id) {
        return setModelAndViewDataForReviewRequest(View.CODE_REVIEW_REQUEST_FORM, id);
    }

    @PostMapping(Endpoint.REVIEW_REQUEST_FORM + "/{id}")
    public ModelAndView saveOrUpdateReviewRequest(ReviewRequest reviewRequest, @PathVariable Long id) {
        feedbackService.updateMessageAndLinkOnReviewRequest(reviewRequest);
        return new ModelAndView("redirect:" + Endpoint.REVIEW_REQUEST_CARD + "/" + id);
    }

    @GetMapping(Endpoint.REVIEW_REQUEST_CARD + "/{id}")
    public ModelAndView openReviewRequestCard(@PathVariable Long id) {

        return setModelAndViewDataForReviewRequest(View.CODE_REVIEW_REQUEST_CARD, id);
    }

    @PostMapping(Endpoint.REVIEW_REQUEST_CARD + "/{id}") // TODO: 28/05/20 split this to methods, add privileges
    public ModelAndView redirectToReview(@PathVariable Long id,
                                         @RequestParam Optional<String> addReview,
                                         @RequestParam Optional<String> saveReview,
                                         @RequestParam Optional<String> closeRequest,
                                         Optional<Review> review,
                                         Optional<ReviewRequest> reviewRequest,
                                         Optional<ReviewResult> reviewResultOnClosing,
                                         @RequestParam Optional<Long> reviewRequestId) {

        if (addReview.isPresent()) {
            ModelAndView mov = setModelAndViewDataForReviewRequest(View.CODE_REVIEW_FORM, id);
            Review newReview = new Review();
            if(mov.getModel().get("reviewRequest") != null) {
                ReviewRequest revReq = (ReviewRequest) mov.getModel().get("reviewRequest");
                newReview.setReviewRequestId(revReq.getId());
                newReview.setReviewedEntityId(revReq.getReviewedEntityId());
                newReview.setItemId(revReq.getItemId());
            }
            mov.addObject("review", newReview);
            return mov;
        } else if (saveReview.isPresent()) {
            Review newReview = review.get();
            newReview.setReviewerProfileId(profileService.getCurrentUserProfile().getId());
            feedbackService.saveReview(review.get());
            ModelAndView mov = setModelAndViewDataForReviewRequest(View.CODE_REVIEW_REQUEST_CARD, id);
            return mov;
        } else if (closeRequest.isPresent()) {
            // TODO: 28/05/20 в этом месте не должно быть Optional<Review> review, но он приходит пустой с айдишником
            // TODO: 28/05/20 и еще в этом месте не байндятся поля ReviewRequest из формы: поля из формы приходит null, поэтому использую одно поле с неймом для статуса ревью.
            if (userAccessService.isCurrentUserIsAdmin()) {
                ReviewRequest reviewRequestToClose = reviewRequest.get();
                reviewRequestToClose.setReviewResultOnClosing(reviewResultOnClosing.get());
                feedbackService.closeReviewRequest(reviewRequestToClose);
                ModelAndView mov = setModelAndViewDataForReviewRequest(View.CODE_REVIEW_REQUEST_CARD, id);
                return mov;
            }
            ModelAndView mov = userDataToModelService.setData(new ModelAndView());
            mov.setViewName(View.PAGE_404);
            return mov;
        }
        else {
            ModelAndView mov = userDataToModelService.setData(new ModelAndView());
            mov.setViewName(View.PAGE_404);
            return mov;
        }
    }


    private ModelAndView setModelAndViewDataForReviewRequest(String viewName, Long id) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Optional<ReviewRequest> reviewRequest = feedbackService.getReviewRequestById(id);
        setModelDataForReviewRequest(mov, reviewRequest);
        if (reviewRequest.isPresent()) {
            mov.setViewName(viewName);
        } else {
            mov.setViewName(View.PAGE_404);
        }
        return mov;
    }

    private ModelAndView setModelDataForReviewRequest(ModelAndView mov, Optional<ReviewRequest> reviewRequest) {
        if (reviewRequest.isPresent()) {
            // TODO: 28/05/20 consider to move all of this into one class (item, codeReviewRequest)?
            ReviewRequest revReq = reviewRequest.get();
            Profile requesterProfile = profileService.findProfileByProfileId(revReq.getRequesterProfileId()).orElseGet(Profile::new);
            Item item = feedbackService.getItemByReviewRequest(revReq);
            String reviewedEntityLink = LinkGenerator.getLinkTo(item.getReviewedEntity());
            String requesterProfileLink = LinkGenerator.getLinkTo(requesterProfile);

            mov.addObject("reviewRequest", revReq);
            mov.addObject("item", item);
            mov.addObject("reviewedLink", reviewedEntityLink);
            mov.addObject("requesterProfile", requesterProfile);
            mov.addObject("requesterProfileLink", requesterProfileLink);
        }
        return mov;

    }

}

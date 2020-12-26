package by.jrr.feedback.controller;

import by.jrr.auth.configuration.annotations.AtLeatStudent;
import by.jrr.auth.service.UserAccessService;
import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.LinkGenerator;
import by.jrr.constant.View;
import by.jrr.feedback.bean.*;
import by.jrr.feedback.elements.RequestForReviewDto;
import by.jrr.feedback.mappers.RequestForReviewDtoMapper;
import by.jrr.feedback.service.FeedbackService;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfilePossessesService;
import by.jrr.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    ProfilePossessesService pss;

    @AtLeatStudent
    @PostMapping(Endpoint.REQUEST_FOR_REVIEW)
    public ModelAndView updateRequestForReview(
            @RequestParam Map<String, String> requestForReviewDtoMap,
            @RequestParam(value = "command", required = false) String command) {
        RequestForReviewDto requestForReviewDto = RequestForReviewDtoMapper.OF.paramMapToRequestForReviewDto(requestForReviewDtoMap);
        switch (command) {
            case "save":
                ReviewRequest requestForReview = feedbackService.createNewRequestForReview(requestForReviewDto);
                return new ModelAndView("redirect:" + Endpoint.REVIEW_REQUEST_CARD + "/" + requestForReview.getId());
            case "update":
                feedbackService.updateRequestForReview(requestForReviewDto);
                return new ModelAndView("redirect:" + Endpoint.REVIEW_REQUEST_CARD + "/" + requestForReviewDto.getId());
            case "delete":
                feedbackService.deleteRequestForReview(requestForReviewDto.getId());
                return new ModelAndView("redirect:" + Endpoint.REVIEW_REQUEST_LIST);
            default:
                return new ModelAndView("redirect:/400");
        }

    }


    @AtLeatStudent
    @GetMapping(Endpoint.REVIEW_REQUEST_FORM + "/{id}")
    public ModelAndView createNewReviewRequest(@PathVariable Long id) {
        return setModelAndViewDataForReviewRequest(View.CODE_REVIEW_REQUEST_FORM, id);
    }

    @AtLeatStudent
    @PostMapping(Endpoint.REVIEW_REQUEST_FORM + "/{id}")
    public ModelAndView saveOrUpdateReviewRequest(ReviewRequest reviewRequest, @PathVariable Long id) {
        feedbackService.updateMessageAndLinkOnReviewRequest(reviewRequest);
        return new ModelAndView("redirect:" + Endpoint.REVIEW_REQUEST_CARD + "/" + id);
    }

    @AtLeatStudent
    @GetMapping(Endpoint.REVIEW_REQUEST_CARD + "/{id}")
    public ModelAndView openReviewRequestCard(@PathVariable Long id) {
        return setModelAndViewDataForReviewRequest(View.CODE_REVIEW_REQUEST_CARD, id);
    }

    @AtLeatStudent
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
            if (mov.getModel().get("reviewRequest") != null) {
                ReviewRequest revReq = (ReviewRequest) mov.getModel().get("reviewRequest");
                newReview.setReviewRequestId(revReq.getId());
                newReview.setReviewedEntityId(revReq.getReviewedEntityId());
                newReview.setItemId(revReq.getItemId());

            }
            mov.addObject("review", newReview);
            return mov;
        } else if (saveReview.isPresent()) {
            review.get().setCreatedDate(LocalDateTime.now());
            Review newReview = review.get();
            newReview.setReviewerProfileId(profileService.getCurrentUserProfile().getId());
            feedbackService.saveReview(review.get());
            ModelAndView mov = setModelAndViewDataForReviewRequest(View.CODE_REVIEW_REQUEST_CARD, id);
            return new ModelAndView("redirect:" + Endpoint.REVIEW_REQUEST_CARD + "/" + id);
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
            return new ModelAndView("redirect:" + Endpoint.REVIEW_REQUEST_CARD + "/" + id);
        } else {
            ModelAndView mov = userDataToModelService.setData(new ModelAndView());
            mov.setViewName(View.PAGE_404);
            return new ModelAndView("redirect:" + Endpoint.REVIEW_REQUEST_CARD + "/" + id);
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
            revReq.setItem(item);
            String reviewedEntityLink = LinkGenerator.getLinkTo(item.getReviewedEntity());
            String requesterProfileLink = LinkGenerator.getLinkTo(requesterProfile);

            mov.addObject("reviewRequest", revReq);
            mov.addObject("requestForReviewDto", RequestForReviewDtoMapper.OF.reviewRequestToRequestForReviewDto(revReq));
            mov.addObject("item", item);
            mov.addObject("reviewedLink", reviewedEntityLink);
            mov.addObject("requesterProfile", requesterProfile);
            mov.addObject("requesterProfileLink", requesterProfileLink);
            mov.addObject("Endpoint", new Endpoint());
            mov.addObject(
                    "requestsForSameIssue",
                    feedbackService.findAllRequestsForReviewByItemId(item.getId())
                            .stream()
                            .filter(rr -> !rr.getId().equals(revReq.getId()))
                            .sorted(Comparator.comparing(ReviewRequest::getCreatedDate).reversed())
                            .limit(20)//todo: add pagination
                            .collect(Collectors.toList()));
        }
        return mov;

    }

}

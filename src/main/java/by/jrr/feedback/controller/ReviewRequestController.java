package by.jrr.feedback.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.LinkGenerator;
import by.jrr.constant.View;
import by.jrr.feedback.bean.Item;
import by.jrr.feedback.bean.Review;
import by.jrr.feedback.bean.ReviewRequest;
import by.jrr.feedback.service.FeedbackService;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfileService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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


    @GetMapping(Endpoint.REVIEW_REQUEST_FORM+"/{id}")
    public ModelAndView createNewReviewRequest(@PathVariable Long id) {
        return setModelAndViewDataForReviewRequest(View.CODE_REVIEW_REQUEST_FORM, id);
    }

    @PostMapping(Endpoint.REVIEW_REQUEST_FORM+"/{id}")
    public ModelAndView saveOrUpdateReviewRequest(ReviewRequest reviewRequest, @PathVariable Long id) {
        feedbackService.saveOrUpdateReviewRequest(reviewRequest);
        return new ModelAndView("redirect:" + Endpoint.REVIEW_REQUEST_CARD+"/"+id);
    }

    @GetMapping(Endpoint.REVIEW_REQUEST_CARD+"/{id}")
    public ModelAndView openReviewRequestCard(@PathVariable Long id) {
        return setModelAndViewDataForReviewRequest(View.CODE_REVIEW_REQUEST_CARD, id);
    }

    private ModelAndView setModelAndViewDataForReviewRequest(String viewName, Long id) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Optional<ReviewRequest> reviewRequest = feedbackService.getReviewRequestById(id);
        if (reviewRequest.isPresent()) {
            // TODO: 28/05/20 consider to move all of this into one class (item, codeReviewRequest)?
            ReviewRequest revReq = reviewRequest.get();
            Profile requesterProfile = profileService.findProfileByProfileId(revReq.getRequesterProfileId()).orElseGet(Profile::new);
            Item item = feedbackService.getItemByReviewRequest(revReq);
            String reviewedEntityLink = LinkGenerator.getLinkTo(item.getReviewedEntity());
            String requesterProfileLink = LinkGenerator.getLinkTo(requesterProfile);

            requesterProfile.getUser().getFullUserName();

            mov.addObject("reviewRequest", revReq);
            mov.addObject("item", item);
            mov.addObject("reviewedLink", reviewedEntityLink);
            mov.addObject("requesterProfile", requesterProfile);
            mov.addObject("requesterProfileLink", requesterProfileLink);

            mov.setViewName(viewName);
        } else {
            mov.setViewName(View.PAGE_404);
        }
        return mov;
    }

}

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
public class ReviewRequestListController {

    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    FeedbackService feedbackService;
    @Autowired
    ProfileService profileService;
    @Autowired
    UserAccessService userAccessService;


    @GetMapping(Endpoint.REVIEW_REQUEST_LIST)
    public ModelAndView openProfileTable(@RequestParam Optional<Integer> page,
                                         @RequestParam Optional<Integer> elem,
                                         @RequestParam Optional<String> searchTerm) {

        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.addObject("reviewRequestPage", feedbackService.findAllReviewRequestPageable(page, elem, searchTerm));
        mov.addObject("searchTerm", searchTerm.orElse(""));
        mov.setViewName(View.CODE_REVIEW_REQUEST_LIST);
        return mov;
    }

}

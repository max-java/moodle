package by.jrr.feedback.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.feedback.bean.ReviewRequest;
import by.jrr.feedback.bean.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class ReviewRequestController {

    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    FeedbackService feedbackService;


    @GetMapping(Endpoint.REVIEW_REQUEST_FORM+"/{id}")
    public ModelAndView createNewReviewRequest(@PathVariable Long id) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Optional<ReviewRequest> reviewRequest = feedbackService.getReviewRequestById(id);
        if (reviewRequest.isPresent()) {
            mov.addObject("reviewRequest", reviewRequest.get());
            mov.setViewName(View.CODE_REVIEW_REQUEST_FORM);
        } else {
            mov.setViewName(View.PAGE_404);
        }
        return mov;
    }
}

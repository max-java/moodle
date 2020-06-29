package by.jrr.moodle.controller;

import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.configuration.annotations.AdminOnly;
import by.jrr.auth.configuration.annotations.AtLeatStudent;
import by.jrr.auth.service.UserAccessService;
import by.jrr.auth.service.UserDataToModelService;
import by.jrr.auth.service.UserService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.feedback.bean.ReviewRequest;
import by.jrr.feedback.service.FeedbackService;
import by.jrr.moodle.bean.PracticeQuestion;
import by.jrr.moodle.service.PracticeQuestionService;
import by.jrr.profile.service.ProfilePossessesService;
import by.jrr.profile.service.ProfileService;
import com.sun.xml.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class PracticeQuestionController { // TODO: 30/05/20 revise and make clear spellink: now it is just a copy of issue controller

    @Autowired
    PracticeQuestionService practiceQuestionService;
    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    FeedbackService feedbackService;
    @Autowired
    ProfilePossessesService pss;
    @Autowired
    UserAccessService userAccessService;
    @Autowired
    UserService userService;

    @AdminOnly
    @GetMapping(Endpoint.PRACTICE)
    public ModelAndView createNewIssue() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.addObject("issue", PracticeQuestion.builder().build());
        mov.addObject("edit", true);
        mov.setViewName(View.PRACTICE);
        return mov;
    }

    @GetMapping(Endpoint.PRACTICE + "/{practiceId}")
    public ModelAndView openIssueByIssueId(@PathVariable Long practiceId) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Optional<PracticeQuestion> issue = practiceQuestionService.findById(practiceId);
        if (issue.isPresent()) {
            if (userAccessService.isCurrentUserIsAdmin() || pss.isUserHasAccessToPractice(issue.get())) {
                mov.addObject("issue", issue.get());
                mov.setViewName(View.PRACTICE);
            } else {
                mov.setViewName(View.PAGE_404); // TODO: 24/06/20 replace with 403 TODO: 24/06/20 create 403 view
            }

        } else {
            mov.setStatus(HttpStatus.NOT_FOUND);
            mov.setViewName(View.PAGE_404);
        }
        return mov;
    }

    @AdminOnly
    @PostMapping(Endpoint.PRACTICE)
    public ModelAndView saveNewIssue(PracticeQuestion issue) {
        issue = practiceQuestionService.create(issue);
        return new ModelAndView("redirect:" + Endpoint.PRACTICE + "/" + issue.getId());
    }


    @PostMapping(Endpoint.PRACTICE + "/{practiceId}")
    public ModelAndView updateIssue(PracticeQuestion issue, HttpServletRequest request,
                                    @PathVariable Long practiceId,
                                    @RequestParam(value = "edit", required = false) boolean edit,
                                    @RequestParam Optional<String> requestForReview) {

        if(requestForReview.isPresent()) {
            if (pss.isUserHasAccessToPractice(issue) &&
                    (// TODO: 28/06/20 move to accessService and consider accesses
                        userService.isCurrentUserHasRole(UserRoles.ROLE_STUDENT)
                        || userService.isCurrentUserHasRole(UserRoles.ROLE_ALUMNUS)
                        || userService.isCurrentUserHasRole(UserRoles.ROLE_ADMIN)
                    )
                )
            {
                return redirectToCodeReview(practiceId, issue, request);
            }
        }

        if (userAccessService.isCurrentUserIsAdmin()) {
            ModelAndView mov = userDataToModelService.setData(new ModelAndView());
            mov.setViewName(View.PRACTICE);
            if (edit && UserAccessService.hasRole(UserRoles.ROLE_ADMIN)) {
                Optional<PracticeQuestion> issueToUpdate = practiceQuestionService.findById(issue.getId());
                if (issueToUpdate.isPresent()) {
                    mov.addObject("issue", issueToUpdate.get());
                    mov.addObject("edit", true);
                } else { // TODO: 11/05/20 impossible situation, but should be logged
                    mov.setViewName(View.PAGE_404);
                }
            } else if (UserAccessService.hasRole(UserRoles.ROLE_ADMIN)) {
                issue = practiceQuestionService.update(issue);
                return new ModelAndView("redirect:" + Endpoint.PRACTICE + "/" + issue.getId());

            }
            return mov;
            // TODO: 11/05/20 replace if-else with private methods
        }
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.setViewName(View.PAGE_404); // TODO: 29/06/20  replace with 403
        return mov;
    }


    private ModelAndView redirectToCodeReview(Long issueId, PracticeQuestion issue, HttpServletRequest request) {
        issue = practiceQuestionService.findById(issueId).get();
        ReviewRequest reviewRequest = feedbackService.createNewReviewRequest(issue);
        return new ModelAndView("redirect:" + Endpoint.REVIEW_REQUEST_FORM+"/"+reviewRequest.getId());
    }
}

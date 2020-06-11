package by.jrr.project.controller;

import by.jrr.auth.configuration.annotations.AtLeatStudent;
import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.feedback.bean.ReviewRequest;
import by.jrr.feedback.service.FeedbackService;
import by.jrr.project.bean.Issue;
import by.jrr.project.service.IssueService;
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
public class IssueController {

    @Autowired
    IssueService issueService;
    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    FeedbackService feedbackService;

    @GetMapping(Endpoint.PROJECT+"/{id}"+Endpoint.ISSUE)
    public ModelAndView createNewIssue(@PathVariable Long id) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.addObject("issue", Issue.builder().projectId(id).build());
        mov.addObject("edit", true);
        mov.setViewName(View.ISSUE);
        return mov;
    }

    @GetMapping(Endpoint.PROJECT+"/{id}"+Endpoint.ISSUE + "/{issueId}")
    public ModelAndView openIssueByIssueId(@PathVariable Long issueId, @PathVariable Long id) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Optional<Issue> issue = issueService.findByIssueId(issueId);
        if (issue.isPresent()) {
            mov.addObject("issue", issue.get());
            mov.setViewName(View.ISSUE);
        } else {
            mov.setStatus(HttpStatus.NOT_FOUND);
            mov.setViewName(View.PAGE_404);
        }
        return mov;
    }

    @AtLeatStudent
    @PostMapping(Endpoint.PROJECT+"/{id}"+Endpoint.ISSUE)
    public ModelAndView saveNewIssue(Issue issue, @PathVariable Long id) {
        issue = issueService.createOrUpdate(issue);
        return new ModelAndView("redirect:" + Endpoint.PROJECT+"/"+issue.getProjectId()+Endpoint.ISSUE + "/" + issue.getIssueId());
    }

    @AtLeatStudent
    @PostMapping(Endpoint.PROJECT+"/{id}"+Endpoint.ISSUE + "/{issueId}")
    public ModelAndView updateIssue(Issue issue, HttpServletRequest request,
                                    @PathVariable Long issueId, @PathVariable Long id,
                                    @RequestParam(value = "edit", required = false) boolean edit,
                                    @RequestParam Optional<String> requestForReview) {

        if(requestForReview.isPresent()) {

            return redirectToCodeReview(issueId, issue, request);
        }

        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.setViewName(View.ISSUE);
        if (edit) {
            Optional<Issue> issueToUpdate = issueService.findByIssueId(issue.getIssueId());
            if (issueToUpdate.isPresent()) {
                mov.addObject("issue", issueToUpdate.get());
                mov.addObject("edit", true);
            } else { // TODO: 11/05/20 impossible situation, but should be logged
                mov.setViewName(View.PAGE_404);
            }
        } else {
            issue = issueService.createOrUpdate(issue);
            return new ModelAndView("redirect:" + Endpoint.PROJECT+"/"+issue.getProjectId()+Endpoint.ISSUE + "/" + issue.getIssueId());

        }
        return mov;
        // TODO: 11/05/20 replace if-else with private methods
    }

    @GetMapping(Endpoint.ISSUE_LIST)
    public ModelAndView findAll(@PathVariable(required = false) String page, @PathVariable(required = false) String size) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Page<Issue> issuePage = issueService.findAll(page, size);
        mov.addObject("issuePage", issuePage);
        mov.setViewName(View.ISSUE_LIST);
        return mov;
    }

    private ModelAndView redirectToCodeReview(Long issueId, Issue issue, HttpServletRequest request) {
        issue = issueService.findByIssueId(issueId).get();
        ReviewRequest reviewRequest = feedbackService.createNewReviewRequest(issue);
        return new ModelAndView("redirect:" + Endpoint.REVIEW_REQUEST_FORM+"/"+reviewRequest.getId());
    }
}

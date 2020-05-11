package by.jrr.project.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
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

import java.util.Optional;

@Controller
public class IssueController {

    @Autowired
    IssueService issueService;
    @Autowired
    UserDataToModelService userDataToModelService;

    @GetMapping(Endpoint.ISSUE)
    public ModelAndView createNewIssue() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.addObject("issue", new Issue());
        mov.addObject("edit", true);
        mov.setViewName(View.ISSUE);
        return mov;
    }

    @GetMapping(Endpoint.ISSUE + "/{issueId}")
    public ModelAndView openQAndAById(@PathVariable Long issueId) {
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

    @PostMapping(Endpoint.ISSUE)
    public ModelAndView saveNewIssue(Issue issue) {
        issue = issueService.createOrUpdate(issue);
        return new ModelAndView("redirect:" + Endpoint.ISSUE + "/" + issue.getIssueId());
    }

    @PostMapping(Endpoint.ISSUE + "/{issueId}")
    public ModelAndView updateIssue(Issue issue,
                                    @PathVariable Long issueId,
                                     @RequestParam(value = "edit", required = false) boolean edit
                                    ) {
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
            return new ModelAndView("redirect:" + Endpoint.ISSUE + "/" + issue.getIssueId());

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
}

package by.jrr.portfolio.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.feedback.bean.ReviewRequest;
import by.jrr.feedback.service.FeedbackService;
import by.jrr.portfolio.bean.Subject;
import by.jrr.portfolio.service.SubjectService;
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
public class SubjectController {

    @Autowired
    SubjectService subjectService;
    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    FeedbackService feedbackService;

    @GetMapping(Endpoint.DOMAIN+"/{id}"+Endpoint.SUBJECT)
    public ModelAndView createNewSubject(@PathVariable Long id) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.addObject("subject", Subject.builder().domainId(id).build());
        mov.addObject("edit", true);
        mov.setViewName(View.SUBJECT);
        return mov;
    }

    @GetMapping(Endpoint.DOMAIN+"/{id}"+Endpoint.SUBJECT + "/{subjectId}")
    public ModelAndView openSubjectBySubjectId(@PathVariable Long subjectId, @PathVariable Long id) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Optional<Subject> subject = subjectService.findBySubjectId(subjectId);
        if (subject.isPresent()) {
            mov.addObject("subject", subject.get());
            mov.setViewName(View.SUBJECT);
        } else {
            mov.setStatus(HttpStatus.NOT_FOUND);
            mov.setViewName(View.PAGE_404);
        }
        return mov;
    }

    @PostMapping(Endpoint.DOMAIN+"/{id}"+Endpoint.SUBJECT)
    public ModelAndView saveNewSubject(Subject subject, @PathVariable Long id) {
        subject = subjectService.createOrUpdate(subject);
        return new ModelAndView("redirect:" + Endpoint.DOMAIN+"/"+subject.getDomainId()+Endpoint.SUBJECT + "/" + subject.getSubjectId());
    }

    @PostMapping(Endpoint.DOMAIN+"/{id}"+Endpoint.SUBJECT + "/{subjectId}")
    public ModelAndView updateSubject(Subject subject, HttpServletRequest request,
                                    @PathVariable Long subjectId, @PathVariable Long id,
                                    @RequestParam(value = "edit", required = false) boolean edit,
                                    @RequestParam Optional<String> requestForReview) {

        if(requestForReview.isPresent()) {

            return redirectToCodeReview(subjectId, subject, request);
        }

        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.setViewName(View.SUBJECT);
        if (edit) {
            Optional<Subject> subjectToUpdate = subjectService.findBySubjectId(subject.getSubjectId());
            if (subjectToUpdate.isPresent()) {
                mov.addObject("subject", subjectToUpdate.get());
                mov.addObject("edit", true);
            } else { // TODO: 11/05/20 impossible situation, but should be logged
                mov.setViewName(View.PAGE_404);
            }
        } else {
            subject = subjectService.createOrUpdate(subject);
            return new ModelAndView("redirect:" + Endpoint.DOMAIN+"/"+subject.getDomainId()+Endpoint.SUBJECT + "/" + subject.getSubjectId());

        }
        return mov;
        // TODO: 11/05/20 replace if-else with private methods
    }

    @GetMapping(Endpoint.SUBJECT_LIST)
    public ModelAndView findAll(@PathVariable(required = false) String page, @PathVariable(required = false) String size) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Page<Subject> subjectPage = subjectService.findAll(page, size);
        mov.addObject("subjectPage", subjectPage);
        mov.setViewName(View.SUBJECT_LIST);
        return mov;
    }

    private ModelAndView redirectToCodeReview(Long subjectId, Subject subject, HttpServletRequest request) {
        subject = subjectService.findBySubjectId(subjectId).get();
        ReviewRequest reviewRequest = feedbackService.createNewReviewRequest(subject);
        return new ModelAndView("redirect:" + Endpoint.REVIEW_REQUEST_FORM+"/"+reviewRequest.getId());
    }
}

package by.jrr.moodle.controller;

import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.configuration.annotations.AdminOnly;
import by.jrr.auth.configuration.annotations.AtLeatStudent;
import by.jrr.auth.service.UserAccessService;
import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.feedback.bean.ReviewRequest;
import by.jrr.feedback.service.FeedbackService;
import by.jrr.moodle.bean.PracticeQuestion;
import by.jrr.moodle.service.PracticeQuestionService;
import by.jrr.profile.service.ProfilePossessesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Controller
public class PracticeQuestionListController { // TODO: 30/05/20 revise and make clear spellink: now it is just a copy of issue controller

    @Autowired
    PracticeQuestionService practiceQuestionService;
    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    FeedbackService feedbackService;
    @Autowired
    ProfilePossessesService pss;

    @GetMapping(Endpoint.PRACTICE_LIST)
    public ModelAndView findAll(@RequestParam Optional<Integer> page,
                                @RequestParam Optional<Integer> elem,
                                @RequestParam Optional<String> searchTerm) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Page<PracticeQuestion> issuePage = practiceQuestionService.findAllPageable(page, elem, searchTerm);

        mov.addObject("issuePage", issuePage);

        String searchWords = "";        // TODO: 03/06/20 consider to move this to service
        if(searchTerm.isPresent()) {    // TODO: 03/06/20 and implement the same fix in other pageable and searchable
            searchWords = UriUtils.encode(searchTerm.get(), StandardCharsets.UTF_8);
        }
        mov.addObject("searchTerm", searchWords);

        mov.setViewName(View.PRACTICE_LIST);
        return mov;
    }
}

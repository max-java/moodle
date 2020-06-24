package by.jrr.moodle.controller;

import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.configuration.annotations.AdminOnly;
import by.jrr.auth.configuration.annotations.AtLeatStudent;
import by.jrr.auth.service.UserAccessService;
import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.moodle.bean.Lecture;
import by.jrr.moodle.service.LectureService;
import by.jrr.profile.service.ProfilePossessesService;
import by.jrr.statistic.bean.TrackStatus;
import by.jrr.statistic.service.UserProgressService;
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
public class LectureController {

    @Autowired
    LectureService lectureService;
    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    UserProgressService userProgressService;
    @Autowired
    ProfilePossessesService pss;

    @GetMapping(Endpoint.LECTURE)
    public ModelAndView createNewTopic() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.addObject("topic", new Lecture());
        mov.addObject("edit", true);
        mov.setViewName(View.LECTURE);
        return mov;
    }

    @GetMapping(Endpoint.LECTURE + "/{id}")
    public ModelAndView openTopicById(@PathVariable Long id) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Optional<Lecture> topic = lectureService.findById(id);
        if (topic.isPresent()) {
            mov.addObject("topic", topic.get());
            TrackStatus trackStatus = userProgressService.getUserProfileForTrackable(topic.get());
            if(trackStatus.equals(TrackStatus.NONE)) {
                userProgressService.saveProgress(topic.get(), TrackStatus.READ); // TODO: 31/05/20 consider to move this to model
            }
            mov.addObject("trackStatus", userProgressService.getUserProfileForTrackable(topic.get()));
            mov.setViewName(View.LECTURE);
        } else {
            mov.setStatus(HttpStatus.NOT_FOUND);
            mov.setViewName(View.PAGE_404);
        }
        return mov;
    }

    @AdminOnly
    @PostMapping(Endpoint.LECTURE)
    public ModelAndView saveNewTopic(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "subtitle", required = false) String subtitle,
            @RequestParam(value = "text", required = false) String text
    ) {
        Lecture topic = lectureService.create(Lecture.builder()
                .title(title)
                .subtitle(subtitle)
                .text(text)
                .build());
        return new ModelAndView("redirect:" + Endpoint.LECTURE + "/" + topic.getId());
    }

    @AtLeatStudent
    @PostMapping(Endpoint.LECTURE + "/{id}")
    public ModelAndView saveNewTopic(@PathVariable Long id,
                                     @RequestParam(value = "title", required = false) String title,
                                     @RequestParam(value = "subtitle", required = false) String subtitle,
                                     @RequestParam(value = "text", required = false) String text,
                                     @RequestParam(value = "edit", required = false) boolean edit,
                                     @RequestParam Optional<String> setLearned,
                                     @RequestParam Optional<String> save
                                    ) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.setViewName(View.LECTURE);
        if (edit && UserAccessService.hasRole(UserRoles.ROLE_ADMIN)) {
            Optional<Lecture> topic = lectureService.findById(id);
            if (topic.isPresent()) {

                mov.addObject("topic", topic.get());
                mov.addObject("edit", true);
                mov.addObject("trackStatus", userProgressService.getUserProfileForTrackable(topic.get()));

            } else { // TODO: 11/05/20 impossible situation, but should be logged
                mov.setViewName(View.PAGE_404);
            }
        } else if (save.isPresent() && UserAccessService.hasRole(UserRoles.ROLE_ADMIN)) {
            Lecture topic = lectureService.update(Lecture.builder()
                    .title(title)
                    .subtitle(subtitle)
                    .text(text)
                    .Id(id)
                    .build());

            mov.addObject("topic", topic);
            mov.addObject("edit", false);
            mov.addObject("trackStatus", userProgressService.getUserProfileForTrackable(topic));
        } else if(setLearned.isPresent()) {
            Optional<Lecture> topic = lectureService.findById(id);
            if (topic.isPresent()) {
                userProgressService.saveProgress(topic.get(), TrackStatus.LEARNED);

                mov.addObject("topic", topic.get());
                mov.addObject("edit", false);
                mov.addObject("trackStatus", userProgressService.getUserProfileForTrackable(topic.get()));

            } else {
                mov.setViewName(View.PAGE_404);
            }
        }
        return mov;
        // TODO: 11/05/20 replace if-else with private methods
    }

    @GetMapping(Endpoint.LECTURE_LIST)
    public ModelAndView findAll(@PathVariable(required = false) String page, @PathVariable(required = false) String size) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Page<Lecture> topicList = lectureService.findAllPageable(page, size);
        mov.addObject("topicList", topicList);
        mov.setViewName(View.LECTURE_LIST);
        return mov;
    }
}

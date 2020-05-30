package by.jrr.moodle.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.moodle.bean.Lecture;
import by.jrr.moodle.bean.Topic;
import by.jrr.moodle.service.LectureService;
import by.jrr.moodle.service.TopicService;
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

    @GetMapping(Endpoint.LECTURE)
    public ModelAndView createNewTopic() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.addObject("topic", new Topic());
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
            mov.setViewName(View.LECTURE);
        } else {
            mov.setStatus(HttpStatus.NOT_FOUND);
            mov.setViewName(View.PAGE_404);
        }
        return mov;
    }

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

    @PostMapping(Endpoint.LECTURE + "/{id}")
    public ModelAndView saveNewTopic(@PathVariable Long id,
                                     @RequestParam(value = "title", required = false) String title,
                                     @RequestParam(value = "subtitle", required = false) String subtitle,
                                     @RequestParam(value = "text", required = false) String text,
                                     @RequestParam(value = "edit", required = false) boolean edit
                                    ) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.setViewName(View.LECTURE);
        if (edit) {
            Optional<Lecture> topic = lectureService.findById(id);
            if (topic.isPresent()) {
                mov.addObject("topic", topic.get());
                mov.addObject("edit", true);
            } else { // TODO: 11/05/20 impossible situation, but should be logged
                mov.setViewName(View.PAGE_404);
            }
        } else {
            Lecture topic = lectureService.update(Lecture.builder()
                    .title(title)
                    .subtitle(subtitle)
                    .text(text)
                    .Id(id)
                    .build());
            mov.addObject("topic", topic);
            mov.addObject("edit", false);
        }
        return mov;
        // TODO: 11/05/20 replace if-else with private methods
    }

    @GetMapping(Endpoint.LECTURE_LIST)
    public ModelAndView findAll(@PathVariable(required = false) String page, @PathVariable(required = false) String size) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Page<Lecture> topicList = lectureService.findAll(page, size);
        mov.addObject("topicList", topicList);
        mov.setViewName(View.LECTURE_LIST);
        return mov;
    }
}

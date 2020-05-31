package by.jrr.moodle.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.moodle.bean.Course;
import by.jrr.moodle.bean.Topic;
import by.jrr.moodle.service.CourseService;
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
public class CourseController { // TODO: 30/05/20  make it like in userProfile & qAndA Trackable

    @Autowired
    CourseService courseService;
    @Autowired
    UserDataToModelService userDataToModelService;

    @GetMapping(Endpoint.COURSE)
    public ModelAndView createNewTopic() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.addObject("topic", new Course());
        mov.addObject("edit", true);
        mov.setViewName(View.TOPIC);
        return mov;
    }

    @GetMapping(Endpoint.COURSE + "/{id}")
    public ModelAndView openTopicById(@PathVariable Long id) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Optional<Course> topic = courseService.findById(id);
        if (topic.isPresent()) {
            mov.addObject("topic", topic.get());
            mov.setViewName(View.TOPIC);
        } else {
            mov.setStatus(HttpStatus.NOT_FOUND);
            mov.setViewName(View.PAGE_404);
        }
        return mov;
    }

    @PostMapping(Endpoint.COURSE)
    public ModelAndView saveNewTopic(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "subtitle", required = false) String subtitle,
            @RequestParam(value = "text", required = false) String text
    ) {
        Course topic = courseService.create(Course.builder()
                .title(title)
                .subtitle(subtitle)
                .text(text)
                .build());
        return new ModelAndView("redirect:" + Endpoint.COURSE + "/" + topic.getId());
    }

    @PostMapping(Endpoint.COURSE + "/{id}")
    public ModelAndView saveNewTopic(@PathVariable Long id,
                                     @RequestParam(value = "title", required = false) String title,
                                     @RequestParam(value = "subtitle", required = false) String subtitle,
                                     @RequestParam(value = "text", required = false) String text,
                                     @RequestParam(value = "edit", required = false) boolean edit
                                    ) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.setViewName(View.COURSE);
        if (edit) {
            Optional<Course> topic = courseService.findById(id);
            if (topic.isPresent()) {
                mov.addObject("topic", topic.get());
                mov.addObject("edit", true);
            } else { // TODO: 11/05/20 impossible situation, but should be logged
                mov.setViewName(View.PAGE_404);
            }
        } else {
            Course topic = courseService.update(Course.builder()
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

    @GetMapping(Endpoint.COURSE_LIST)
    public ModelAndView findAll(@PathVariable(required = false) String page, @PathVariable(required = false) String size) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Page<Course> topicList = courseService.findAll(page, size);
        mov.addObject("topicList", topicList);
        mov.setViewName(View.COURSE_LIST);
        return mov;
    }
}

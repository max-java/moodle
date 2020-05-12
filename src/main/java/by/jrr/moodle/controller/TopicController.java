package by.jrr.moodle.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.moodle.bean.Topic;
import by.jrr.moodle.repository.TopicRepository;
import by.jrr.moodle.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class TopicController {

    @Autowired
    TopicService topicService;
    @Autowired
    UserDataToModelService userDataToModelService;

    @GetMapping(Endpoint.TOPIC)
    public ModelAndView createNewTopic() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.addObject("topic", new Topic());
        mov.addObject("edit", true);
        mov.setViewName(View.TOPIC);
        return mov;
    }

    @GetMapping(Endpoint.TOPIC + "/{id}")
    public ModelAndView openTopicById(@PathVariable Long id) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Optional<Topic> topic = topicService.findById(id);
        if (topic.isPresent()) {
            mov.addObject("topic", topic.get());
            mov.setViewName(View.TOPIC);
        } else {
            mov.setStatus(HttpStatus.NOT_FOUND);
            mov.setViewName(View.PAGE_404);
        }
        return mov;
    }

    @PostMapping(Endpoint.TOPIC)
    public ModelAndView saveNewTopic(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "subtitle", required = false) String subtitle,
            @RequestParam(value = "text", required = false) String text
    ) {
        Topic topic = topicService.create(Topic.builder()
                .title(title)
                .subtitle(subtitle)
                .text(text)
                .build());
        return new ModelAndView("redirect:" + Endpoint.TOPIC + "/" + topic.getId());
    }

    @PostMapping(Endpoint.TOPIC + "/{id}")
    public ModelAndView saveNewTopic(@PathVariable Long id,
                                     @RequestParam(value = "title", required = false) String title,
                                     @RequestParam(value = "subtitle", required = false) String subtitle,
                                     @RequestParam(value = "text", required = false) String text,
                                     @RequestParam(value = "edit", required = false) boolean edit
                                    ) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.setViewName(View.TOPIC);
        if (edit) {
            Optional<Topic> topic = topicService.findById(id);
            if (topic.isPresent()) {
                mov.addObject("topic", topic.get());
                mov.addObject("edit", true);
            } else { // TODO: 11/05/20 impossible situation, but should be logged
                mov.setViewName(View.PAGE_404);
            }
        } else {
            Topic topic = topicService.update(Topic.builder()
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

    @GetMapping(Endpoint.TOPIC_LIST)
    public ModelAndView findAll(@PathVariable(required = false) String page, @PathVariable(required = false) String size) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Page<Topic> topicList = topicService.findAll(page, size);
        mov.addObject("topicList", topicList);
        mov.setViewName(View.TOPIC_LIST);
        return mov;
    }
}

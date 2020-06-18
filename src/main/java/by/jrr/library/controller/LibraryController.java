package by.jrr.library.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.feedback.service.FeedbackService;
import by.jrr.moodle.service.PracticeQuestionService;
import by.jrr.profile.service.ProfilePossessesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Controller
public class LibraryController {

//    @Autowired
//    LibraryService libraryService;
//    @Autowired
//    UserDataToModelService userDataToModelService;
//    @Autowired
//    StreamAndTeamSubscriberService streamAndTeamSubscriberService;
//    @GetMapping(Endpoint.BOOK_LIST)
//    public ModelAndView findAll(@PathVariable(required = false) String page, @PathVariable(required = false) String size) {
//        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
//        Page<Topic> bookList = libraryService.findAll(page, size);
//        mov.addObject("bookList", bookList);
//        mov.setViewName(View.BOOK_LIST);
//        return mov;
//        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
//        Page<Book> topicList = courseService.findAll(page, size);
//        mov.addObject("topicList", topicList);
//        mov.setViewName(View.COURSE_LIST);
//        return mov;
//    }
}

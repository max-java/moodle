package by.jrr.library.controller;

import by.jrr.auth.configuration.annotations.AdminOnly;
import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.feedback.service.FeedbackService;
import by.jrr.library.bean.Book;
import by.jrr.library.service.BookService;
import by.jrr.profile.service.ProfilePossessesService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class BookController {
    @Autowired
    BookService bookService;
    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    FeedbackService feedbackService;
    @Autowired
    ProfilePossessesService pss;

    @AdminOnly
    @GetMapping(Endpoint.BOOK)
    public ModelAndView createNewIssue() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.addObject("issue", Book.builder().build());
        mov.addObject("edit", true);
        mov.setViewName(View.BOOK);
        return mov;
    }

    @GetMapping(Endpoint.BOOK + "/{bookId}")
    public ModelAndView openIssueByIssueId(@PathVariable Long bookId) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Optional<Book> issue = bookService.findById(bookId);

        if (issue.isPresent()) {
            mov.addObject("issue", issue.get());
            mov.setViewName(View.BOOK);
        } else {
            mov.setStatus(HttpStatus.NOT_FOUND);
            mov.setViewName(View.PAGE_404);
        }
        return mov;
    }

    @AdminOnly
    @PostMapping(Endpoint.BOOK)
    public ModelAndView saveNewIssue(Book issue) {
        issue = bookService.create(issue);
        return new ModelAndView("redirect:" + Endpoint.BOOK + "/" + issue.getId());
    }


    @PostMapping(Endpoint.BOOK + "/{bookId}")
    public ModelAndView updateIssue(Book issue, HttpServletRequest request,
                                    @PathVariable Long bookId,
                                    @RequestParam(value = "edit", required = false) boolean edit) {


        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.setViewName(View.BOOK);

        if (edit) {
            Optional<Book> issueToUpdate = bookService.findById(issue.getId());
            if (issueToUpdate.isPresent()) {
                mov.addObject("issue", issueToUpdate.get());
                mov.addObject("edit", true);
            } else { // TODO: 11/05/20 impossible situation, but should be logged
                mov.setViewName(View.PAGE_404);
            }
        } else {
            issue = bookService.update(issue);
            return new ModelAndView("redirect:" + Endpoint.BOOK + "/" + issue.getId());

        }
        return mov;
        // TODO: 11/05/20 replace if-else with private methods
    }
}

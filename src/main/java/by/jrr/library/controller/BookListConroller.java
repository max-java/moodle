package by.jrr.library.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.feedback.service.FeedbackService;
import by.jrr.library.bean.Book;
import by.jrr.library.service.BookService;
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
public class BookListConroller {
    @Autowired
    BookService bookService;
    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    FeedbackService feedbackService;
    @Autowired
    ProfilePossessesService pss;

    @GetMapping(Endpoint.BOOK_LIST)
    public ModelAndView findAll(@RequestParam Optional<Integer> page,
                                @RequestParam Optional<Integer> elem,
                                @RequestParam Optional<String> searchTerm) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Page<Book> issuePage = bookService.findAllPageable(page, elem, searchTerm);

        mov.addObject("issuePage", issuePage);

        String searchWords = "";        // TODO: 03/06/20 consider to move this to service
        if(searchTerm.isPresent()) {    // TODO: 03/06/20 and implement the same fix in other pageable and searchable
            searchWords = UriUtils.encode(searchTerm.get(), StandardCharsets.UTF_8);
        }
        mov.addObject("searchTerm", searchWords);

        mov.setViewName(View.BOOK_LIST);
        return mov;
    }
}

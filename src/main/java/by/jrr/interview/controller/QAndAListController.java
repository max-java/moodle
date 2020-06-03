package by.jrr.interview.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.interview.bean.QAndA;
import by.jrr.interview.service.QAndAService;
import by.jrr.statistic.service.UserProgressService;
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
public class QAndAListController {

    @Autowired
    QAndAService qAndAService;
    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    UserProgressService userProgressService;

    @GetMapping(Endpoint.Q_AND_A_LIST)
    public ModelAndView findAll(@RequestParam Optional<Integer> page,
                                @RequestParam Optional<Integer> elem,
                                @RequestParam Optional<String> searchTerm) {

        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Page<QAndA> qAndAPage = qAndAService.findAllPageable(page, elem, searchTerm);
        mov.addObject("qAndAPage", qAndAPage);
        mov.addObject("totalQuestions", qAndAService.totalQuestions());

        String searchWords = "";        // TODO: 03/06/20 consider to move this to service
        if(searchTerm.isPresent()) {    // TODO: 03/06/20 and implement the same fix in other pageable and searchable
            searchWords = UriUtils.encode(searchTerm.get(), StandardCharsets.UTF_8);
        }
        mov.addObject("searchTerm", searchWords);

        mov.setViewName(View.Q_AND_A_LIST);
        return mov;
    }
}

package by.jrr.interview.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.interview.QAndAService;
import by.jrr.interview.bean.QAndA;
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
public class QAndAController {

    @Autowired
    QAndAService qAndAService;
    @Autowired
    UserDataToModelService userDataToModelService;

    @GetMapping(Endpoint.Q_AND_A)
    public ModelAndView createNewQAndA() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.addObject("qAndA", new QAndA());
        mov.addObject("edit", true);
        mov.setViewName(View.Q_AND_A);
        return mov;
    }

    @GetMapping(Endpoint.Q_AND_A + "/{id}")
    public ModelAndView openQAndAById(@PathVariable Long id) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Optional<QAndA> qAndA = qAndAService.findById(id);
        if (qAndA.isPresent()) {
            mov.addObject("qAndA", qAndA.get());
            mov.setViewName(View.Q_AND_A);
        } else {
            mov.setStatus(HttpStatus.NOT_FOUND);
            mov.setViewName(View.PAGE_404);
        }
        return mov;
    }

    @PostMapping(Endpoint.Q_AND_A)
    public ModelAndView saveNewQAndA(
            @RequestParam(value = "question", required = false) String question,
            @RequestParam(value = "answer", required = false) String answer
    ) {
        QAndA qAndA = qAndAService.create(QAndA.builder()
                .question(question)
                .answer(answer)
                .build());
        return new ModelAndView("redirect:" + Endpoint.Q_AND_A + "/" + qAndA.getId());
    }

    @PostMapping(Endpoint.Q_AND_A + "/{id}")
    public ModelAndView saveNewQAndA(@PathVariable Long id,
                                     @RequestParam(value = "question", required = false) String question,
                                     @RequestParam(value = "answer", required = false) String answer,
                                     @RequestParam(value = "edit", required = false) boolean edit
                                    ) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.setViewName(View.Q_AND_A);
        if (edit) {
            Optional<QAndA> qAndA = qAndAService.findById(id);
            if (qAndA.isPresent()) {
                mov.addObject("qAndA", qAndA.get());
                mov.addObject("edit", true);
            } else { // TODO: 11/05/20 impossible situation, but should be logged
                mov.setViewName(View.PAGE_404);
            }
        } else {
            QAndA qAndA = qAndAService.update(QAndA.builder()
                    .question(question)
                    .answer(answer)
                    .Id(id)
                    .build());
            mov.addObject("qAndA", qAndA);
            mov.addObject("edit", false);
        }
        return mov;
        // TODO: 11/05/20 replace if-else with private methods
    }

    @GetMapping(Endpoint.Q_AND_A_LIST)
    public ModelAndView findAll(@PathVariable(required = false) String page, @PathVariable(required = false) String size) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Page<QAndA> qAndAPage = qAndAService.findAll(page, size);
        mov.addObject("qAndAPage", qAndAPage);
        mov.setViewName(View.Q_AND_A_LIST);
        return mov;
    }
}

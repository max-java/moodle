package by.jrr.interview.controller;

import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.configuration.annotations.AdminOnly;
import by.jrr.auth.configuration.annotations.AtLeatStudent;
import by.jrr.auth.service.UserAccessService;
import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.interview.service.QAndAService;
import by.jrr.interview.bean.QAndA;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfilePossessesService;
import by.jrr.profile.service.ProfileService;
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
public class QAndAController {

    @Autowired
    QAndAService qAndAService;
    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    UserProgressService userProgressService;
    @Autowired
    ProfilePossessesService pss;


    @AdminOnly
    @GetMapping(Endpoint.Q_AND_A)
    public ModelAndView createNewQAndA() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.addObject("qAndA", new QAndA());
        mov.addObject("edit", true);
        mov.setViewName(View.Q_AND_A);
        return mov;
    }

    @AtLeatStudent
    @GetMapping(Endpoint.Q_AND_A + "/{id}")
    public ModelAndView openQAndAById(@PathVariable Long id) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Optional<QAndA> qAndA = qAndAService.findById(id);
        if (qAndA.isPresent()) {
            mov.addObject("qAndA", qAndA.get());
            TrackStatus trackStatus = userProgressService.getUserProfileForTrackable(qAndA.get());
            if(trackStatus.equals(TrackStatus.NONE)) {
                userProgressService.saveProgress(qAndA.get(), TrackStatus.READ);
            }
            mov.addObject("trackStatus", userProgressService.getUserProfileForTrackable(qAndA.get()));
            mov.setViewName(View.Q_AND_A);
        } else {
            mov.setStatus(HttpStatus.NOT_FOUND);
            mov.setViewName(View.PAGE_404);
        }
        return mov;
    }

    @AdminOnly
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

    @AtLeatStudent
    @PostMapping(Endpoint.Q_AND_A + "/{id}")
    public ModelAndView saveNewQAndA(@PathVariable Long id,
                                     @RequestParam(value = "question", required = false) String question,
                                     @RequestParam(value = "answer", required = false) String answer,
                                     @RequestParam(value = "edit", required = false) boolean edit,
                                     @RequestParam Optional<String> setLearned,
                                     @RequestParam Optional<String> save
                                    ) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.setViewName(View.Q_AND_A);
        if (edit && UserAccessService.hasRole(UserRoles.ROLE_ADMIN)) {
            Optional<QAndA> qAndA = qAndAService.findById(id);
            if (qAndA.isPresent()) {

                mov.addObject("qAndA", qAndA.get());
                mov.addObject("edit", true);
                mov.addObject("trackStatus", userProgressService.getUserProfileForTrackable(qAndA.get()));

            } else { // TODO: 11/05/20 impossible situation, but should be logged
                mov.setViewName(View.PAGE_404);
            }
        } else if(save.isPresent()  && UserAccessService.hasRole(UserRoles.ROLE_ADMIN)) {
            QAndA qAndA = qAndAService.update(QAndA.builder()
                    .question(question)
                    .answer(answer)
                    .Id(id)
                    .build());

            mov.addObject("qAndA", qAndA);
            mov.addObject("edit", false);
            mov.addObject("trackStatus", userProgressService.getUserProfileForTrackable(qAndA));

        } else if(setLearned.isPresent()) {
            Optional<QAndA> qAndA = qAndAService.findById(id);
            if (qAndA.isPresent()) {
                userProgressService.saveProgress(qAndA.get(), TrackStatus.LEARNED);

                mov.addObject("qAndA", qAndA.get());
                mov.addObject("edit", false);
                mov.addObject("trackStatus", userProgressService.getUserProfileForTrackable(qAndA.get()));

            } else {
                mov.setViewName(View.PAGE_404);
            }
        }
        return mov;
        // TODO: 11/05/20 replace if-else with private methods
    }
}

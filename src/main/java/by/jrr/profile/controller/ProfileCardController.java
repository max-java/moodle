package by.jrr.profile.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.project.bean.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProfileCardController {

    @Autowired
    UserDataToModelService userDataToModelService;

    @GetMapping(Endpoint.PROFILE_CARD)
    public ModelAndView createNewProject() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.setViewName(View.PROFILE_CARD);
        return mov;
    }
}

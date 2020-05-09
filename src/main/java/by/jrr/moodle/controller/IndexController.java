package by.jrr.moodle.controller;

import by.jrr.auth.service.UserDataToModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @Autowired
    UserDataToModelService userDataToModelService;

    @GetMapping("/")
    public ModelAndView openIndex() {
        ModelAndView modelAndView = userDataToModelService.setIsAuthenticated(new ModelAndView());
        modelAndView.setViewName("starter");
        return modelAndView;
    }
}

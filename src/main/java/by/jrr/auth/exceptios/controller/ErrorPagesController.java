package by.jrr.auth.exceptios.controller;

import by.jrr.auth.service.UserDataToModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorPagesController {

    @Autowired
    UserDataToModelService userDataToModelService;

    @RequestMapping("/403")
    public ModelAndView accessDenied() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.setViewName("403");
        return mov;
    }

    @RequestMapping("/404")
    public ModelAndView pageNotFound() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.setViewName("404");
        return mov;
    }

    @RequestMapping("/400")
    public ModelAndView badRequest() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.setViewName("400");
        return mov;
    }
}

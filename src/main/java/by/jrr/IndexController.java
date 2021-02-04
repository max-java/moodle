package by.jrr;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    ProfileService profileService;

    @GetMapping("/")
    public ModelAndView openIndex(HttpServletRequest request) {
        ModelAndView modelAndView = userDataToModelService.setDataWithSessionData(new ModelAndView(), request);
        modelAndView.setViewName("starter");
        modelAndView.addObject("streamList", profileService.findStreamsWhereEnrollIsOpen());

        return modelAndView;
    }
}

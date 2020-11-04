package by.jrr;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    ProfileService profileService;

    @GetMapping("/")
    public ModelAndView openIndex() {
        ModelAndView modelAndView = userDataToModelService.setData(new ModelAndView());
        modelAndView.setViewName("starter");
        modelAndView.addObject("streamList", profileService.findStreamsWhereEnrollIsOpen());

        return modelAndView;
    }
}

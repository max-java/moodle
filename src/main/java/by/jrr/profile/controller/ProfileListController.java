package by.jrr.profile.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.auth.service.UserService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProfileListController {

    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    ProfileService profileService;


    @GetMapping(Endpoint.PROFILE_LIST)
    public ModelAndView openProfileTable() {
        // check if all profiles has been created
        // TODO: 25/05/20 this should be moved near user creation
        profileService.createProfileForUsers();
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.addObject("profilePage", profileService.findAllProfilesPageable(null, null));
        mov.setViewName(View.PROFILE_LIST);
        return mov;
    }
}

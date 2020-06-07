package by.jrr.profile.controller;

import by.jrr.auth.bean.User;
import by.jrr.auth.bean.UserFields;
import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.service.UserDataToModelService;
import by.jrr.auth.service.UserService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class TeamCardController {
    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    UserService userService;
    @Autowired
    ProfileService profileService;

    @GetMapping(Endpoint.REGISTER_TEAM)
    public ModelAndView openTeamRegistrationForm() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.addObject("user", new User());
        mov.setViewName(View.STREAM_TEAM_REGISTRATION_FORM);
        return mov;
    }


    @PostMapping(Endpoint.REGISTER_TEAM)
    public ModelAndView createNewTeamUser(@Valid User user,
                                      BindingResult bindingResult,
                                      @RequestParam Optional<String> retypePassword) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        User userExists = userService.findUserByUserName(user.getUserName());
        if (userExists != null) {
            bindingResult
                    .rejectValue("userName", "error.user",
                            "Такая группа уже существует. Пожалуйста, придумайте что-то новое");
        }
        if (!retypePassword.isPresent() || !user.getPassword().equals(retypePassword.get())) {
            bindingResult
                    .rejectValue("password", "error.user",
                            "Пароли не совпадают");
        }
        if (bindingResult.hasFieldErrors(UserFields.USER_NAME.getAsString())
                || bindingResult.hasFieldErrors(UserFields.NAME.getAsString())
                || bindingResult.hasFieldErrors(UserFields.PASSWORD.getAsString())){
            mov.setViewName(View.STREAM_TEAM_REGISTRATION_FORM);
        } else{
            user.setEmail(profileService.getCurrentUserProfile().getUser().getEmail()); // TODO: 07/06/20 consider to consider
            user.setPhone(profileService.getCurrentUserProfile().getUser().getPhone()); // TODO: 07/06/20 consider to consider
            User newUser = userService.saveUser(user, Optional.of(UserRoles.TEAM));
            Profile profile = profileService.createAndSaveProfileForUser(newUser);
            return new ModelAndView("redirect:" + Endpoint.PROFILE_CARD + "/" + profile.getId());
        }
        return mov;
    }
}

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class StreamCardController {
    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    UserService userService;
    @Autowired
    ProfileService profileService;

    @GetMapping(Endpoint.REGISTER_STREAM)
    public ModelAndView openStreamRegistrationForm() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.addObject("user", new User());
        mov.setViewName(View.STREAM_TEAM_REGISTRATION_FORM);
        return mov;
    }
    @GetMapping(Endpoint.REGISTER_STREAM+"/{courseId}")
    public ModelAndView openStreamRegistrationForm(@PathVariable Long courseId) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.addObject("user", new User());
        mov.addObject("courseId", courseId);
        mov.setViewName(View.STREAM_TEAM_REGISTRATION_FORM);
        return mov;
    }


    @PostMapping(Endpoint.REGISTER_STREAM)
    public ModelAndView createNewStreamUser(@Valid User user,
                                      BindingResult bindingResult,
                                      @RequestParam Optional<String> retypePassword,
                                      @RequestParam Optional<Long> courseId
    ) {
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
            User newUser = userService.saveUser(user, Optional.of(UserRoles.STREAM));
            Profile profile = profileService.createAndSaveProfileForUser(newUser, courseId.orElse(null));
            return new ModelAndView("redirect:" + Endpoint.PROFILE_CARD + "/" + profile.getId());
        }
        return mov;
    }
    @PostMapping(Endpoint.REGISTER_STREAM+"/{courseId}") // TODO: 10/06/20 clean duplicates!!!
    public ModelAndView createNewStreamUser2(@Valid User user,
                                            BindingResult bindingResult,
                                            @RequestParam Optional<String> retypePassword,
                                            @RequestParam Optional<Long> courseId
    ) {
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
            User newUser = userService.saveUser(user, Optional.of(UserRoles.STREAM));
            Profile profile = profileService.createAndSaveProfileForUser(newUser, courseId.orElse(null));
            return new ModelAndView("redirect:" + Endpoint.PROFILE_CARD + "/" + profile.getId());
        }
        return mov;
    }
}

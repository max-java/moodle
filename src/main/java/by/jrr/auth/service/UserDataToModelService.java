package by.jrr.auth.service;

import by.jrr.auth.bean.User;
import by.jrr.auth.bean.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

@Service
public class UserDataToModelService {

    @Autowired
    UserService userService;

    public ModelAndView setData(ModelAndView mov) {
        setIsAuthenticated(mov);
        setUserNameAndLastName(mov);
        setListOfUsers(mov);
        return mov;
    }

    public ModelAndView setIsAuthenticated(ModelAndView modelAndView) {
        boolean isUserAuthenticated = false;
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                //when Anonymous Authentication is enabled
                && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            isUserAuthenticated = true;
            modelAndView = setUserNameAndLastName(modelAndView);
        }
        return modelAndView.addObject(UserData.IS_AUTHENTICATED.name(), isUserAuthenticated);
    }

    public ModelAndView setUserNameAndLastName(ModelAndView modelAndView) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        return modelAndView.addObject(UserData.USER_NAME_AND_LASTNAME.name(), user.getName() + " " + user.getLastName());
    }

    public ModelAndView setListOfUsers(ModelAndView mov) {
        return mov.addObject(UserData.USER_LIST.name(), userService.findAllUsers());
    }
}

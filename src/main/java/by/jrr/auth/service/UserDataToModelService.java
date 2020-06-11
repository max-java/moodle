package by.jrr.auth.service;

import by.jrr.auth.bean.User;
import by.jrr.auth.bean.UserData;
import by.jrr.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class UserDataToModelService {

    @Autowired
    UserService userService;
    @Autowired
    UserAccessService userAccessService;
    @Autowired
    ProfileService profileService; // TODO: 01/06/20 this doesn't belong here. set current user profile otherwise

    public ModelAndView setData(ModelAndView mov) { // TODO: 28/05/20 replace by different methods simmilar to getInstance with zero parameters and cache it
        setIsAuthenticated(mov);
        setCurrentUserProfile(mov);
        setListOfUsers(mov);
        return mov;
    }

    private ModelAndView setIsAuthenticated(ModelAndView modelAndView) {
        return modelAndView.addObject(UserData.IS_AUTHENTICATED.name(), userAccessService.isCurrentUserAuthenticated());
    }

    /** set authenticated user data */
    private ModelAndView setCurrentUserProfile(ModelAndView modelAndView) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        if (user != null) {
            modelAndView.addObject(UserData.USER_NAME_AND_LASTNAME.name(), user.getName() + " " + user.getLastName());
            modelAndView.addObject(UserData.CURRENT_PROFILE.name(), profileService.getCurrentUserProfile()); // TODO: 04/06/20 erase password data
        }
        return modelAndView;
    }

    private ModelAndView setListOfUsers(ModelAndView mov) {
        return mov.addObject(UserData.USER_LIST.name(), userService.findAllUsers());
    }
}

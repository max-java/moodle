package by.jrr.auth.service;

import by.jrr.auth.bean.User;
import by.jrr.auth.bean.UserData;
import by.jrr.balance.bean.Currency;
import by.jrr.balance.bean.OperationRow;
import by.jrr.balance.constant.Http;
import by.jrr.balance.dto.UserBalanceSummaryDto;
import by.jrr.balance.service.OperationRowService;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class UserDataToModelService {

    @Autowired
    UserService userService;
    @Autowired
    UserAccessService userAccessService;
    @Autowired
    ProfileService profileService; // TODO: 01/06/20 this doesn't belong here. set current user profile otherwise
    @Autowired
    OperationRowService operationRowService;
//    @Autowired
//    KeycloakSecurityContext securityContext;


    public ModelAndView setData(ModelAndView mov) { // TODO: 28/05/20 replace by different methods simmilar to getInstance with zero parameters and cache it
        setIsAuthenticated(mov);
        setCurrentUserProfile(mov);
        setListOfUsers(mov);
        setUserDTOToRegisterAndEnrollModalForm(mov);
        setUserBalance(mov);
        return mov;
    }

    public ModelAndView setDataWithSessionData(ModelAndView mov, HttpServletRequest request) { // TODO: 28/05/20 replace by different methods simmilar to getInstance with zero parameters and cache it
        mov = setData(mov);
        mov.addObject("notification", request.getSession().getAttribute("notification"));
        request.getSession().removeAttribute("notification");
        return mov;
    }



    private ModelAndView setUserDTOToRegisterAndEnrollModalForm(ModelAndView mov) {
        // need to work with quick register form.
        mov.addObject("modalFormUserData", new User());
        return mov;
    }

    private ModelAndView setIsAuthenticated(ModelAndView modelAndView) {
        return modelAndView.addObject(UserData.IS_AUTHENTICATED.name(), userAccessService.isCurrentUserAuthenticated());
    }

    /**
     * set authenticated user data
     */
    private ModelAndView setCurrentUserProfile(ModelAndView modelAndView) {
        Optional<User> user = Optional.empty(); //securityContext.getCurrentMoodleUser();
        if (user.isPresent()) {
            modelAndView.addObject(UserData.USER_NAME_AND_LASTNAME.name(), user.get().getName() + " " + user.get().getLastName());
            modelAndView.addObject(UserData.CURRENT_PROFILE.name(), profileService.getCurrentUserProfile()); // TODO: 04/06/20 erase password data
        }
        return modelAndView;
    }

    private ModelAndView setListOfUsers(ModelAndView mov) {
        return mov.addObject(UserData.USER_LIST.name(), userService.findAllUsers());
    }

    private ModelAndView setUserBalance(ModelAndView mov) {
        mov.addObject("userBalanceTotal", new UserBalanceSummaryDto());
        Profile profile = profileService.getCurrentUserProfile();
        if (profile != null) {
            List<OperationRow> userOperations = operationRowService.getAllOperationsForUser(profile.getId());
            UserBalanceSummaryDto userTotal = operationRowService.getSummariesForProfileOperations(profile.getId(), Currency.BYN);
            operationRowService.calculateAndSetTotalUserSalary(userOperations, userTotal);
            mov.addObject("userBalanceTotal", userTotal);
        }
        return mov;
    }
}

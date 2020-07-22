package by.jrr.profile.admin;

import by.jrr.auth.bean.Role;
import by.jrr.auth.bean.User;
import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.configuration.annotations.AdminOnly;
import by.jrr.auth.exceptios.UserServiceException;
import by.jrr.auth.service.UserDataToModelService;
import by.jrr.auth.service.UserService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.files.service.FileService;
import by.jrr.profile.admin.bean.ErrorsLog;
import by.jrr.profile.admin.bean.UserDTO;
import by.jrr.profile.admin.repo.ErrorsLogRestRepository;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.bean.SubscriptionStatus;
import by.jrr.profile.service.ProfilePossessesService;
import by.jrr.profile.service.ProfileService;
import by.jrr.profile.service.ProfileStatisticService;
import by.jrr.profile.service.StreamAndTeamSubscriberService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class UserRegistrationAdminRestController {

    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    ProfileService profileService;
    @Autowired
    FileService fileService;
    @Autowired
    ProfileStatisticService profileStatisticService;
    @Autowired
    ProfilePossessesService pss;
    @Autowired
    StreamAndTeamSubscriberService streamAndTeamSubscriberService;
    @Autowired
    UserService userService;
    @Autowired
    ErrorsLogRestRepository errorsLogRestRepository;

    @PostMapping(value = Endpoint.REGISTER_USER_ADMIN_REST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO registerNewUserAsAdmin(
            @RequestBody UserDTO user,
            HttpServletResponse response
    ) {

        try {
            userService.registerNewUserAsAdmin(user);
        } catch (Exception ex) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());


            ErrorsLog errorsLog = new ErrorsLog();
            errorsLog.setUserDto(user.toString());
            errorsLog.setTimestamp(LocalDateTime.now());
            errorsLog.setExName(ex.toString());
            errorsLog.setExMessage(ex.getMessage());
            errorsLog.setExStacktrace(ex.getStackTrace());
            errorsLog.getCause().add(ex.getMessage());
            Throwable causes =ex.getCause();
            while (causes != null) {
                errorsLog.getCause().add(causes.getMessage());
                if(errorsLog.getCause().size() >2) {
                    if (errorsLog.getCause().get(errorsLog.getCause().size() - 1)
                            .equals(errorsLog.getCause().get(errorsLog.getCause().size() - 2))) {
                        causes = null;
                    }
                }
                causes = causes.getCause();
            }
            errorsLog = errorsLogRestRepository.save(errorsLog);

            user.setErrors(errorsLog.getCause());

        }
        return user;
    }

}



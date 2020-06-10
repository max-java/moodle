package by.jrr.profile.controller;

import by.jrr.auth.bean.User;
import by.jrr.auth.exceptios.UserServiceException;
import by.jrr.auth.service.UserDataToModelService;
import by.jrr.auth.service.UserService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.files.service.FileService;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfileService;
import by.jrr.profile.service.ProfileStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

// TODO: 10/06/20 I got registration on course at course controller and registration here.
//  Consider to make one controller that holds all registrations and enrolling (subscriptions)
//  subscribe (Записаться) is a key to search

@Controller
public class RegisterAndSubscribeController {

    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    ProfileService profileService;
    @Autowired
    FileService fileService;
    @Autowired
    ProfileStatisticService profileStatisticService;
    @Autowired
    UserService userService;

    @Autowired
    HttpServletRequest request;

    @PostMapping(Endpoint.REGISTER_USER_AND_ENROLL_TO_STREAM)
    // TODO: 09/06/20 request params names against UserFields enum validation
    public ModelAndView registerAndSubscribe(@RequestParam Optional<String> firstAndLastName,
                                             @RequestParam Optional<String> phone,
                                             @RequestParam Optional<String> email,
                                             @RequestParam Optional<Long> courseId
    ) {


        try { // TODO: 10/06/20 encapsulate
            if (firstAndLastName.isPresent()
                    && phone.isPresent()
                    && email.isPresent()) {
                User user = quickRegisterUser(firstAndLastName.get(), phone.get(), email.get());
                Profile newUserProfile = createProfile(user);
                if (courseId.isPresent()) {
                    Optional<Profile> courseProfile = findStreamProfileByCourseId(courseId.get());
                    if(courseProfile.isPresent()) {
                        enroll(courseProfile.get().getId(), newUserProfile.getId());
                        return new ModelAndView("redirect:" + Endpoint.PROFILE_CARD + "/" + courseProfile.get().getId());
                    }
                    // TODO: 10/06/20
                    //  No course or stream id is present.
                    //  No enrolling info. Add Exception or handle otherwise.
                } else {
                    // TODO: 09/06/20
                    //  No course or stream id is present.
                    //  No enrolling info. Add Exception or handle otherwise.
                }

            } else {
                // TODO: 09/06/20
                //  user info incomplete. add validation message and redirect back where it come from
            }
        } catch (Exception ex) {
            // TODO: 10/06/20 add user message from exceptions
            ex.printStackTrace();

        }
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.setViewName(View.PAGE_404);
        return mov;
    }

    private User quickRegisterUser(String firstAndLastName, String phone, String email) throws UserServiceException {
        return userService.quickRegisterUser(firstAndLastName, phone, email);
    }

    private Profile createProfile(User user) {
        return profileService.createAndSaveProfileForUser(user);
    }

    private void enroll(Long streamProfileId, Long currentUserId) {
        profileService.enrollToStreamTeamProfile(streamProfileId, currentUserId);
    }

    private Optional<Profile> findStreamProfileByCourseId(Long courseId) {
        return profileService.findNearestFromNowOpennForEnrolStreamByCourseId(courseId);
    }
}

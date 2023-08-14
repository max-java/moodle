package by.jrr.profile.controller;

import by.jrr.api.model.UserContactsDto;
import by.jrr.auth.bean.User;
import by.jrr.auth.exceptios.UserNameConversionException;
import by.jrr.auth.exceptios.UserServiceException;
import by.jrr.auth.service.UserDataToModelService;
import by.jrr.auth.service.UserService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.files.service.FileService;
import by.jrr.message.service.MessageService;
import by.jrr.moodle.bean.Course;
import by.jrr.moodle.service.CourseService;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.bean.SubscriptionStatus;
import by.jrr.profile.model.SubscriptionDto;
import by.jrr.profile.service.ProfilePossessesService;
import by.jrr.profile.service.ProfileService;
import by.jrr.profile.service.ProfileStatisticService;
import by.jrr.profile.service.SubscriptionService;
import by.jrr.registration.bean.EventType;
import by.jrr.registration.bean.StudentActionToLog;
import by.jrr.registration.service.StudentActionToLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailParseException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.RollbackException;
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
    ProfilePossessesService pss;
    @Autowired
    CourseService courseService;
    @Autowired
    StudentActionToLogService satls;

    @Autowired
    HttpServletRequest request; //todo: looks like this should be deleted?

    @Autowired
    MessageService messageService;

    @Autowired
    SubscriptionService subscriptionService;


    @Deprecated//to be deleted because switching to keycloak
    @PostMapping(Endpoint.REGISTER_USER_AND_ENROLL_TO_STREAM)
    public RedirectView registerAndSubscribe(@RequestParam Optional<String> firstAndLastName,
                                             @RequestParam Optional<String> phone,
                                             @RequestParam Optional<String> email,
                                             @RequestParam Optional<Long> courseId,
                                             @RequestParam Optional<Long> streamId
    ) {

        Long courzeId = null;
        if (courseId.isPresent()) {
            courzeId = courseId.get();
        }
        RedirectView redirectView = new RedirectView();
        Profile courseProfile = new Profile();
        try { // TODO: 10/06/20 encapsulate
            if (firstAndLastName.isPresent()
                    && phone.isPresent()
                    && email.isPresent()) {
//
//
//                User user = quickRegisterUser(firstAndLastName.get(), phone.get(), email.get());
//
//
//
                User user = new User(); //this is only for code to compile while switching to keycloak
                Profile userProfile = profileService.findProfileByUserId(user.getId());

                if (streamId.isPresent()) {
//                    Optional<Profile> courseProfile = findStreamProfileByCourseId(courseId.get());
                    Optional<Profile> courseProfileOp = profileService.findProfileByProfileId(streamId.get());
                    if (courseProfileOp.isPresent()) {
                        courseProfile = courseProfileOp.get();

                        enroll(courseProfile.getId(), userProfile.getId());

                        redirectView.setUrl(Endpoint.PROFILE_CARD + "/" + courseProfile.getId());
                        return redirectView;
                    }
                    // TODO: 10/06/20
                    //  No course or stream id is present.
                    //  No enrolling info. Add Exception or handle otherwise.
                    System.out.println("no cource is present");
                } else {
                    // TODO: 09/06/20
                    //  No course or stream id is present.
                    //  No enrolling info. Add Exception or handle otherwise.
                    System.out.println("no course or stream id is present");
                }

            } else {
                // TODO: 09/06/20
                //  user info incomplete. add validation message and redirect back where it come from
                System.out.println("information incomplite");
            }

        } catch (MailParseException ex) {
            ex.printStackTrace();
//            return handleFormValidationException("Ошибка в email", courzeId);
            redirectView.setUrl(Endpoint.COURSE + "/" + courseProfile.getId());
            return redirectView;
        } catch (TransactionSystemException | RollbackException ex) {
//            return handleFormValidationException("Похоже email неправильный. Если это не так, пожалуйста, свяжитесь с куратором по телефону +37529 3333 600", courzeId);
            redirectView.setUrl(Endpoint.COURSE + "/" + courseProfile.getId());
            return redirectView;
        } catch (Exception ex) {
            ex.printStackTrace();
//            return handleFormValidationException("Неизвестная ошибка. Пожалуйста, свяжитесь с куратором по телефону +37529 3333 600", courzeId);
            redirectView.setUrl(Endpoint.COURSE + "/" + courseProfile.getId());
            return redirectView;
        }
//        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
//        mov.setViewName(View.PAGE_404);
        redirectView.setUrl(Endpoint.COURSE_LIST + "/");
        return redirectView;
    }

    private Profile createProfile(User user) {
        return profileService.createAndSaveProfileForUser(user);
    }

    private void enroll(Long streamProfileId, Long currentUserId) {
        SubscriptionDto.Request subscriptionRequest = new SubscriptionDto.Request();
        subscriptionRequest.setStatus(SubscriptionStatus.REQUESTED);
        subscriptionRequest.setStreamTeamProfileId(streamProfileId);
        subscriptionRequest.setSubscriberProfileId(currentUserId);
        subscriptionService.requestSubscription(subscriptionRequest);
    }

    private Optional<Profile> findStreamProfileByCourseId(Long courseId) {
        return profileService.findNearestFromNowOpennForEnrolStreamByCourseId(courseId);
    }

    private ModelAndView handleFormValidationException(String errorMessage, Long courzeId) {
        // TODO: 10/06/20 add user message from exceptions
        // TODO: 16/06/20 and remove this costyl and courzzzeId .... Ugrrr...
        // TODO: 16/09/20 lools like coud be deleted
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.addObject("error", errorMessage);
        Optional<Course> topic = courseService.findById(courzeId);
        if (topic.isPresent()) {
            mov.addObject("topic", topic.get());
            mov.addObject("user", new User()); // TODO: 10/06/20 is it really need?
            mov.setViewName(View.COURSE);
        } else {
            mov.setStatus(HttpStatus.NOT_FOUND);
            mov.setViewName(View.PAGE_404);
        }
        return mov;
    }
}


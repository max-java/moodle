package by.jrr.profile.controller;

import by.jrr.auth.bean.User;
import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.configuration.annotations.AdminOnly;
import by.jrr.auth.service.UserDataToModelService;
import by.jrr.auth.service.UserService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.files.service.FileService;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.bean.StreamAndTeamSubscriber;
import by.jrr.profile.bean.SubscriptionStatus;
import by.jrr.profile.service.ProfilePossessesService;
import by.jrr.profile.service.ProfileService;
import by.jrr.profile.service.ProfileStatisticService;
import by.jrr.profile.service.StreamAndTeamSubscriberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@CrossOrigin
public class ProfileCardAdminViewController {

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



    @GetMapping(Endpoint.PROFILE_CARD_ADMIN_VIEW + "/{profileId}")
    public ModelAndView openProfileById(@PathVariable Long profileId) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Optional<Profile> profile = profileService.findProfileByProfileId(profileId);
        if (profile.isPresent() && pss.isUserHasAccessToReadProfile(profile.get())) {
            mov.setViewName(View.PROFILE_CARD_ADMIN_VIEW);
            mov.addObject("profile", profile.get());
            mov.addObject("statistic", profileStatisticService.calculateStatisticsForProfile(profileId));
        } else {
            mov.setViewName(View.PAGE_404);
        }
        return mov;
//        <a href="/stream/register">new Stream</a> | <a href="/team/register">new Team</a> // TODO: 07/06/20
    }

    @PostMapping(Endpoint.PROFILE_CARD_ADMIN_VIEW + "/{profileId}")
    public ModelAndView saveProfile(@PathVariable Long profileId,
//                                    Optional<StreamAndTeamSubscriber> streamAndTeamSubscriberOp,  // TODO: 17/06/20 process as BindingResult result (IllegalStateException: Neither BindingResult nor plain target object for bean name 'subscriber' available as request attribute)
                                    @RequestParam Optional<Long> subscriberProfileId,
                                    @RequestParam Optional<String> command
    ) {
        if (command.isPresent() && command.get().equals(Commands.APPROVE_SUBSCRIPTION)) {
            approveSubscription(profileId, subscriberProfileId);
        }
        return new ModelAndView("redirect:" + Endpoint.PROFILE_CARD_ADMIN_VIEW + "/" + profileId);
    }


    private void approveSubscription(Long profileId, Optional<Long> subscriberProfileIdOp) {
        if (subscriberProfileIdOp.isPresent()) {
            streamAndTeamSubscriberService.updateSubscription(profileId, subscriberProfileIdOp.get(), SubscriptionStatus.APPROVED);
        }
    }

    private class Commands {
        public static final String APPROVE_SUBSCRIPTION = "approve";
    }

    @AdminOnly // TODO: 23/06/20 move to appropriate place
    @GetMapping(value = Endpoint.PROFILE_CARD_ADMIN_VIEW + "/api/updateRole/{profileId}", produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody UserRolesDTO updateUserRole(@PathVariable Long profileId,
                                   @RequestParam String userRole) {
        UserRolesDTO userRolesDTO = new UserRolesDTO();
        Optional<Profile> profile = profileService.findProfileByProfileId(profileId);
        if(profile.isPresent()) {
            UserRoles userRoleToUpdate = UserRoles.valueOf(userRole);
            if (profile.get().getUser().hasRole(userRoleToUpdate)) {

                userService.removeRoleFromUser(userRoleToUpdate, profile.get().getUserId());
            } else {
                userService.addRoleToUser(userRoleToUpdate, profile.get().getUserId());
            }
            userRolesDTO.setUserRoles(profile.get().getUser().getAllRoles());
        }
        return userRolesDTO;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @XmlRootElement
    public static class UserRolesDTO {
        String userRoles;
    }

    @AdminOnly // TODO: 23/06/20 move to appropriate place
    @GetMapping(value = Endpoint.PROFILE_CARD_ADMIN_VIEW + "/api/generateNewPassword/{profileId}", produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody UserPasswordDTO generateNewPassword(@PathVariable Long profileId) {
        UserPasswordDTO userPasswordDTO = new UserPasswordDTO();
        Optional<Profile> userProfileOP = profileService.findProfileByProfileId(profileId);
        if(userProfileOP.isPresent()) {
            userPasswordDTO.setPassword(userService.generateNewPasswordForUser(userProfileOP.get().getUserId()));
        } else {
            userPasswordDTO.setPassword("error on updating password");
        }
        return userPasswordDTO;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @XmlRootElement
    public static class UserPasswordDTO {
        String password;
    }
}

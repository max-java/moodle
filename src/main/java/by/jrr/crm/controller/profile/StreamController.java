package by.jrr.crm.controller.profile;

import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.configuration.annotations.AdminOnly;
import by.jrr.auth.service.UserAccessService;
import by.jrr.auth.service.UserDataToModelService;
import by.jrr.auth.service.UserService;
import by.jrr.balance.bean.Contract;
import by.jrr.balance.bean.Currency;
import by.jrr.balance.bean.OperationRow;
import by.jrr.balance.constant.Action;
import by.jrr.balance.constant.FieldName;
import by.jrr.balance.dto.UserBalanceSummaryDto;
import by.jrr.balance.service.ContractService;
import by.jrr.balance.service.OperationCategoryService;
import by.jrr.balance.service.OperationRowService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.LinkGenerator;
import by.jrr.constant.View;
import by.jrr.crm.service.HistoryItemService;
import by.jrr.files.service.FileService;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.bean.SubscriptionStatus;
import by.jrr.profile.bean.TimeLine;
import by.jrr.profile.model.SubscriptionDto;
import by.jrr.profile.service.*;
import by.jrr.registration.bean.EventType;
import by.jrr.registration.service.RedirectionLinkService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@CrossOrigin
public class StreamController {

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
    OperationRowService operationRowService;

    @Autowired
    HistoryItemService historyItemService;

    @Autowired
    TimeLineService timeLineService;

    @Autowired
    ContractService contractService;
    @Autowired
    OperationCategoryService operationCategoryService;
    @Autowired
    RedirectionLinkService redirectionLinkService;
    @Autowired
    UserAccessService userAccessService;

    @GetMapping(Endpoint.PROFILE_CARD_ADMIN_VIEW + "/{profileId}")
    public ModelAndView openProfileById(@PathVariable Long profileId, HttpServletRequest request) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());

        mov.addObject("profile", request.getSession().getAttribute("notification"));
        request.getSession().removeAttribute("notification");

        Optional<Profile> profile = profileService.findProfileByProfileId(profileId);
        if (profile.isPresent() && pss.isUserHasAccessToReadProfile(profile.get())) {

            if (profile.get().getCourseId() != null) { // TODO: 08/10/2020 split to user and stream profiles
                mov.setViewName(View.CRM_VIEW_STREAM_CARD);
            } else {
                mov.setViewName(View.CRM_VIEW_USER_CARD);
            }

            Map<LocalDate, List<TimeLine>> timeline = timeLineService.getTimelineForProfile(profile.get());
            mov.addObject("timeLines", timeline);
            mov.addObject("totalLectures", timeline //@max
                    .entrySet()
                    .stream()
                    .flatMap(a -> a.getValue().stream())
                    .filter(l -> l.getEventType().equals(EventType.LECTURE))
                    .map(l -> l.getUrlToRedirect())
                    .distinct()
                    .count());

            mov.addObject("profile", profile.get());
            mov.addObject("statistic", profileStatisticService.calculateStatisticsForProfile(profileId));

            //set billing tab items
            mov.addObject("Action", new Action());
            mov.addObject("FieldName", new FieldName());
            //blank instances for forms can work to add
            mov.addObject("blankRow", new OperationRow());
            mov.addObject("contract", new Contract());

            mov.addObject("operationRows", operationRowService.getOperationsForStream(profileId));
            mov.addObject("total", operationRowService.summariesForStream(profileId));
            mov.addObject("contracts", contractService.findContractsForStream(profileId));
            mov.addObject("contractTypes", contractService.getContractTypes());

            mov.addObject("history", historyItemService.getHistoryForProfile(profileId));
            mov.addObject("operationCategories", operationCategoryService.getAllOperationCategories());

            //user billing -> should be moved to userProfileController for admins
            //todo create Dto for this ? Rest endpoint? Separate controller for user? Move to separate controller for user.
            List<OperationRow> userOperations = operationRowService.getAllOperationsForUser(profileId);
            UserBalanceSummaryDto userTotal = operationRowService.getSummariesForProfileOperations(profileId, Currency.BYN);
            operationRowService.calculateAndSetTotalUserSalary(userOperations, userTotal);
            mov.addObject("userOperationRows", userOperations);
            mov.addObject("userTotal", userTotal);
            mov.addObject("isUserGetSalary", pss.isUserGetSalary(profile.get()));

            mov.addObject("redirectionLinks", redirectionLinkService.findRedirectionLinksForProfile(profileId));

            mov.addObject("isUserIsAdmin", userAccessService.isCurrentUserIsAdmin()); // TODO: 31/07/20 set here null, or "", or  and see result
            mov.addObject("STUDENT", !(
                    UserAccessService.isUserHasRole(profile.get().getUser(), UserRoles.ROLE_STREAM)
                            || UserAccessService.isUserHasRole(profile.get().getUser(), UserRoles.ROLE_TEAM)
            ));

            mov.addObject("SubscriptionDto", new SubscriptionDto());

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
    public @ResponseBody
    UserRolesDTO updateUserRole(@PathVariable Long profileId,
                                @RequestParam String userRole) {
        UserRolesDTO userRolesDTO = new UserRolesDTO();
        Optional<Profile> profile = profileService.findProfileByProfileId(profileId);
        if (profile.isPresent()) {
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
    @Deprecated
    public static class UserRolesDTO {
        String userRoles;
    }

    @Deprecated
    @AdminOnly
    @GetMapping(value = Endpoint.PROFILE_CARD_ADMIN_VIEW + "/api/generateNewPassword/{profileId}", produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody
    UserPasswordDTO generateNewPassword(@PathVariable Long profileId) {
        UserPasswordDTO userPasswordDTO = new UserPasswordDTO();
        Optional<Profile> userProfileOP = profileService.findProfileByProfileId(profileId);
        if (userProfileOP.isPresent()) {
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
    @Deprecated
    public static class UserPasswordDTO {
        String password;
    }
}

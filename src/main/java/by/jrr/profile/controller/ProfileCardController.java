package by.jrr.profile.controller;

import by.jrr.auth.bean.User;
import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.service.UserAccessService;
import by.jrr.auth.service.UserDataToModelService;
import by.jrr.balance.constant.FieldName;
import by.jrr.constant.Endpoint;
import by.jrr.constant.LinkGenerator;
import by.jrr.constant.View;
import by.jrr.crm.service.HistoryItemService;
import by.jrr.files.service.FileService;
import by.jrr.moodle.service.CourseToLectureService;
import by.jrr.profile.bean.*;
import by.jrr.profile.service.*;
import by.jrr.registration.service.StudentActionToLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static by.jrr.common.MyHeaders.cameFrom;

@Controller
public class ProfileCardController {

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
    CourseToLectureService courseToLectureService;
    @Autowired
    StreamAndTeamSubscriberService streamAndTeamSubscriberService;
    @Autowired
    StudentActionToLogService satls;
    @Autowired
    HistoryItemService historyItemService;
    @Autowired
    UserAccessService userAccessService;
    @Autowired
    TimeLineService timeLineService;

    @GetMapping(Endpoint.PROFILE_CARD + "/{profileId}")
    public ModelAndView openProfileById(@PathVariable Long profileId) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Optional<Profile> profile = profileService.findProfileByProfileId(profileId);

        if (profile.isPresent() && pss.isUserHasAccessToReadProfile(profile.get())) {

            mov.setViewName(View.PROFILE_CARD);

            mov.addObject("profile", profile.get());
            mov.addObject("timeLines", timeLineService.getTimelineForProfile(profile.get()));
            mov.addObject("emptyTimeline", new TimeLine());

            mov.addObject("FieldName", new FieldName());

            mov.addObject("statistic", profileStatisticService.calculateStatisticsForProfile(profileId));
            mov.addObject("isSubscribeAble", isSubscribeAble(profileId)); // TODO: 05/08/20 consider to make it more clearly (var name and behaviour)
            mov.addObject("isUserIsOwner", pss.isCurrentUserOwner(profileId));
            mov.addObject("isUserIsAdmin", userAccessService.isCurrentUserIsAdmin()); // TODO: 31/07/20 set here null, or "", or  and see result
            mov.addObject("streamImage", LinkGenerator.getLinkToUserpic(profile.get()));
            mov.addObject("history", historyItemService.getHistoryForProfile(profileId));
            mov.addObject("STREAM", UserAccessService.isUserHasRole(profile.get().getUser(), UserRoles.ROLE_STREAM));
            mov.addObject("TEAM", UserAccessService.isUserHasRole(profile.get().getUser(), UserRoles.ROLE_TEAM));
            mov.addObject("STUDENT", !(
                            UserAccessService.isUserHasRole(profile.get().getUser(), UserRoles.ROLE_STREAM)
                            || UserAccessService.isUserHasRole(profile.get().getUser(), UserRoles.ROLE_TEAM)
                            ));
            mov.addObject("couldUpdate", profileId.equals(profileService.getCurrentUserProfileId())
                    || pss.isCurrentUserOwner(profileId));

            if (profile.get().getCourseId() != null) {
                mov.addObject("topicList", courseToLectureService.findLecturesForCourse(profile.get().getCourseId(), null));
            }
            else {
                mov.addObject("topicList", new ArrayList<>());
            }

            mov.addObject("bestStudentList", calculateBestStudent(profile.get().getSubscribers()));
            mov.addObject("isUserCanUpdateTimeline", pss.isUserCanUpdateTimelineOn(profile.get()));

        }
        else {
            mov.setViewName(View.PAGE_404);
        }
        return mov;
//        <a href="/stream/register">new Stream</a> | <a href="/team/register">new Team</a> // TODO: 07/06/20
    }

    private List<UserProfileStatisticDTO> calculateBestStudent(List<StreamAndTeamSubscriber> subsribers) {
        List<UserProfileStatisticDTO> statistics = new ArrayList<>();
        for (StreamAndTeamSubscriber subscriber : subsribers) {
            statistics.add(profileService.caclulateStatisticsForUserProfile(subscriber));
        }
        statistics = statistics.stream()
                .filter(a -> a.getLectures().size() > 2)
                .collect(Collectors.toList());
        Collections.sort(statistics);
        Collections.reverse(statistics);

        return statistics;
    }

    private boolean isSubscribeAble(Long profileId) {
//        SUBSCRIBE IS inactive if user is subscribed
//        could not subscribe on self profile
        return profileId.equals(profileService.getCurrentUserProfileId())
                || streamAndTeamSubscriberService.isUserSubscribedForProfile(profileId, profileService.getCurrentUserProfileId());
    }

    @PostMapping(Endpoint.PROFILE_CARD + "/{profileId}")
    public String saveProfile(@PathVariable Long profileId,
                                    @RequestParam Optional<MultipartFile> avatar, // TODO: 04/06/20 handle NPE
                                    @RequestParam Optional<String> saveProfile,
                                    @RequestParam Optional<String> updateProfile,
//                                    Optional<Profile> profile, // TODO: 15/06/20 throws exception on bind LocalDate. Debug and fix it.
                                    @RequestParam Optional<String> subscribe,
                                    @RequestParam Optional<Long> subscriberProfileId,
                                    @RequestParam Optional<String> command,
                                    HttpServletRequest request
    ) {

        if (saveProfile.isPresent() && pss.isCurrentUserOwner(profileId)) { // TODO: 30/07/20 admin should be able to upload avatar
            if (avatar.isPresent()) {
                saveAvatar(avatar, profileId);
            }
        }
        if (updateProfile.isPresent() && pss.isCurrentUserOwner(profileId)) {
            // TODO: 15/06/20 throws exception on bind LocalDate. Debug and fix it. That is why I moved it in ProfileCardUpdateController
        }
        if (subscribe.isPresent()) {
            profileService.enrollToStreamTeamProfile(profileId, profileService.getCurrentUserProfile().getId());
        }
        if (pss.isCurrentUserOwner(profileId)) {
            if (command.isPresent() && command.get().equals(ProfileCardController.Commands.APPROVE_SUBSCRIPTION)) {
                approveSubscription(profileId, subscriberProfileId);
            }
            if (command.isPresent() && command.get().equals(ProfileCardController.Commands.REJECT_SUBSCRIPTION)) {
                rejectSubscription(profileId, subscriberProfileId);
            }
        }

        return "redirect:".concat(cameFrom(request));
    }

    private void approveSubscription(Long profileId, Optional<Long> subscriberProfileIdOp) {
        if (subscriberProfileIdOp.isPresent()) {
            streamAndTeamSubscriberService.updateSubscription(profileId, subscriberProfileIdOp.get(), SubscriptionStatus.APPROVED);
        }
    }

    private void rejectSubscription(Long profileId, Optional<Long> subscriberProfileIdOp) {
        if (subscriberProfileIdOp.isPresent()) {
            streamAndTeamSubscriberService.deleteSubscription(profileId, subscriberProfileIdOp.get());
        }
    }

    private class Commands {
        public static final String APPROVE_SUBSCRIPTION = "approve";
        public static final String REJECT_SUBSCRIPTION = "reject";
    }


    private void saveAvatar(Optional<MultipartFile> avatar, Long profileId) {
        Optional<Profile> profile = profileService.findProfileByProfileId(profileId);
        if (profile.isPresent() && pss.isCurrentUserOwner(profileId)) {
            try {
                Profile updatedProfile = profile.get();
                updatedProfile.setAvatarFileName(fileService.saveUploaded(avatar.get(), Optional.empty()));
                profileService.updateProfile(updatedProfile);
            } catch (IOException e) {
                // TODO: 01/06/20 log exceptions
                e.printStackTrace();
            }
        }
    }
}

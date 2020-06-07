package by.jrr.profile.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.files.service.FileService;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfileService;
import by.jrr.profile.service.ProfileStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Optional;

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

    @GetMapping(Endpoint.PROFILE_CARD + "/{profileId}")
    public ModelAndView openProfileById(@PathVariable Long profileId) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Optional<Profile> profile = profileService.findProfileByProfileId(profileId);
        if (profile.isPresent()) {
            mov.setViewName(View.PROFILE_CARD);
            mov.addObject("profile", profile.get());
            mov.addObject("statistic", profileStatisticService.calculateStatisticsForProfile(profileId));
        } else {
            mov.setViewName(View.PAGE_404);
        }
        return mov;
//        <a href="/stream/register">new Stream</a> | <a href="/team/register">new Team</a> // TODO: 07/06/20
    }

    @PostMapping(Endpoint.PROFILE_CARD + "/{profileId}")
    public ModelAndView saveProfile(@PathVariable Long profileId,
                                    @RequestParam Optional<MultipartFile> avatar, // TODO: 04/06/20 handle NPE
                                    @RequestParam Optional<String> saveProfile,
                                    @RequestParam Optional<String> subscribe
                                    ) {

        if (saveProfile.isPresent()) {
            if (avatar.isPresent()) {
                Optional<Profile> profile = profileService.findProfileByProfileId(profileId);
                if (profile.isPresent()) {
                    try {
                        Profile updatedProfile = profile.get();
                        updatedProfile.setAvatarFileName(fileService.saveUploaded(avatar.get(), Optional.empty()));
                        profileService.saveProfile(updatedProfile);
                    } catch (IOException e) {
                        // TODO: 01/06/20 log exceptions
                        e.printStackTrace();
                    }
                }
            }
        }
        if(subscribe.isPresent()) {
            profileService.requestSubcriptionToProfile(profileId, profileService.getCurrentUserProfile().getId());
        }
        return new ModelAndView("redirect:" + Endpoint.PROFILE_CARD + "/" + profileId);
    }
}

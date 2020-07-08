package by.jrr.profile.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.files.service.FileService;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfilePossessesService;
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
import java.time.LocalDate;
import java.util.Optional;

@Controller
public class ProfileCardUpdateController {

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

    @PostMapping(Endpoint.PROFILE_CARD + "/{profileId}/update")
    public ModelAndView updateProfile(@PathVariable Long profileId,
                                      @RequestParam Optional<String> dateStart,
                                      @RequestParam Optional<String> dateEnd,
                                      @RequestParam Optional<String> about,
                                      @RequestParam Optional<String> telegramLink,
                                      @RequestParam Optional<String> telegramLinkName,
                                      @RequestParam Optional<String> zoomLink,
                                      @RequestParam Optional<String> zoomLinkName,
                                      @RequestParam Optional<String> gitLink,
                                      @RequestParam Optional<String> gitUsername,
                                      @RequestParam Optional<String> updateProfile
    ) {

        if (updateProfile.isPresent()) {
            if(profileId.equals(profileService.getCurrentUserProfileId())
                    || pss.isCurrentUserOwner(profileId)) {

                Optional<Profile> profileOp = profileService.findProfileByProfileId(profileId);
                if (profileOp.isPresent()) {
                    Profile profile = profileOp.get();
                    if (dateStart.isPresent()) {
                        try {
                            profile.setDateStart(LocalDate.parse(dateStart.get()));
                        } catch (Exception ex) {
                            // TODO: 16/06/20 log exception
                        }
                    }
                    if (dateEnd.isPresent()) {
                        try {
                            profile.setDateEnd(LocalDate.parse(dateEnd.get()));
                        } catch (Exception ex) {
                            // TODO: 16/06/20 log exception
                        }
                    }
                    if (about.isPresent()) {
                        profile.setAbout(about.get());
                    }
                    if (telegramLink.isPresent()) {
                        profile.setTelegramLink(telegramLink.get());
                    }
                    if (telegramLinkName.isPresent()) {
                        profile.setTelegramLinkText(telegramLinkName.get());
                    }
                    if (zoomLink.isPresent()) {
                        profile.setZoomLink(zoomLink.get());
                    }
                    if (zoomLinkName.isPresent()) {
                        profile.setZoomLinkText(zoomLinkName.get());
                    }
                    if (gitLink.isPresent()) {
                        profile.setGitLink(gitLink.get());
                    }
                    if (gitUsername.isPresent()) {
                        profile.setGitUsername(gitUsername.get());
                    }
                    profileService.updateProfile(profile);
                }
            }
        }
        return new ModelAndView("redirect:" + Endpoint.PROFILE_CARD + "/" + profileId);
    }
}

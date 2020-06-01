package by.jrr.profile.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfileService;
import by.jrr.project.bean.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping(Endpoint.PROFILE_CARD + "/{profileId}")
    public ModelAndView openProfileById(@PathVariable Long profileId) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        Optional<Profile> profile = profileService.findProfileByProfileId(profileId);
        if (profile.isPresent()) {
            mov.setViewName(View.PROFILE_CARD);
            mov.addObject("profile", profile.get());
        } else {
            mov.setViewName(View.PAGE_404);
        }
        return mov;
    }

    @PostMapping(Endpoint.PROFILE_CARD + "/{profileId}")
    public ModelAndView saveProfile(@PathVariable Long profileId,
                                    @RequestParam MultipartFile avatar,
                                    @RequestParam Optional<String> saveProfile) {

        if (saveProfile.isPresent()) {
            Optional<Profile> profile = profileService.findProfileByProfileId(profileId);
            if (profile.isPresent()) {
                try {
                    Profile updatedProfile = profile.get();
                    updatedProfile.setAvatar(avatar.getBytes());
                    profileService.saveProfile(updatedProfile);
                } catch (IOException e) {
                    // TODO: 01/06/20 log it
                    e.printStackTrace();
                }
            }
        }

        return new ModelAndView("redirect:" + Endpoint.PROFILE_CARD + "/" + profileId);

    }

    // TODO: 01/06/20 consider move in separate controller (see Profile.class comment)

    @RequestMapping(value = "/image/{profileId}") // TODO: 01/06/20 add to constants
    @ResponseBody
    public byte[] helloWorld(@PathVariable Long profileId)  {
        Optional<Profile> profile = profileService.findProfileByProfileId(profileId);
        if (profile.isPresent()) {
            if (profile.get().getAvatar() != null) {
                return profile.get().getAvatar();
            }
        }

        return "no image".getBytes(); // TODO: 01/06/20 return default image
    }
}

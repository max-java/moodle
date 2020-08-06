package by.jrr.crm.service;

import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfileService;
import by.jrr.telegram.bot.service.TgUserService;
import by.jrr.telegram.model.TgUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class TgUserListService {

    @Autowired
    TgUserService tgUserService;
    @Autowired
    ProfileService profileService;

    public List<Profile> findProfilesWithTgUser() {
        List<Profile> profiles = new LinkedList<>();
        List<TgUser> tgUsers = tgUserService.findAll();
        for (TgUser tgUser : tgUsers
        ) {
            Optional<Profile> profileOp = profileService.findProfileByProfileId(tgUser.getProfileId());
            if(profileOp.isPresent()) {
                Profile profile = profileOp.get();
                profile.setTgUser(tgUser);
                profiles.add(profile);
            }
        }
        return profiles;
    }
}

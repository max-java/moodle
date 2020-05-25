package by.jrr.profile.service;

import by.jrr.auth.bean.User;
import by.jrr.auth.service.UserService;
import by.jrr.interview.bean.QAndA;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    UserService userService;
    @Autowired
    ProfileRepository profileRepository;

    public Page<Profile> findAllProfilesPageable(String page, String items) {
        Page<Profile> profilePage;
        try {
            profilePage = profileRepository.findAll(PageRequest.of(Integer.valueOf(page), Integer.valueOf(items)));
        } catch (Exception ex) {
            profilePage = profileRepository.findAll(PageRequest.of(Integer.valueOf(3), Integer.valueOf(3)));
        }
        profilePage.forEach(a -> setUserDataToProfile(a));
        for (Profile p : profilePage) {
            p.getUser().getName();
        }

        profilePage.getTotalElements();
        System.out.println("profilePage.hasPrevious() = " + profilePage.hasPrevious());
        System.out.println("profilePage.hasNext() = " + profilePage.hasNext());

        return profilePage;
    }

    private void setUserDataToProfile(Profile profile) {
        userService.findUserById(profile.getId()).ifPresent(user -> profile.setUser(user));
    }

    public void createProfileForUsers() {
        // at first I create User, then profile for user.
        // TODO: 25/05/20 this should be removed and placed near User registration
        List<User> users = userService.findAllUsers();
        for (User user : users) {
            Optional<Profile> profile = profileRepository.findByUserId(user.getId());
            if (!profile.isPresent()) {
                createAndSaveProfileForUser(user);
            }
        }
    }
    private Profile createAndSaveProfileForUser(User user) {
        return profileRepository.save(Profile.builder().userId(user.getId()).build());
    }
}

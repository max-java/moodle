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
import java.util.function.Supplier;

@Service
public class ProfileService {
    private final Supplier<Integer> DEFAULT_PAGE_NUMBER = () -> 1;
    private final Supplier<Integer> DEFAULT_ELEMENTS_PER_PAGE = () -> 3;

    @Autowired
    UserService userService;
    @Autowired
    ProfileRepository profileRepository;

    public Page<Profile> findAllProfilesPageable(Optional<Integer> userFriendlyNumberOfPage,
                                                 Optional<Integer> numberOfElementsPerPage,
                                                 Optional<String> searchTerm) {
        // pages are begins from 0, but userFriendly is to begin from 1
        int page = userFriendlyNumberOfPage.orElseGet(DEFAULT_PAGE_NUMBER) - 1;
        int elem = numberOfElementsPerPage.orElseGet(DEFAULT_ELEMENTS_PER_PAGE);
        Page<Profile> profilePage = profileRepository.findAll(PageRequest.of(page, elem)); // TODO: 26/05/20 test for NPE
        profilePage.forEach(a -> setUserDataToProfile(a));
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

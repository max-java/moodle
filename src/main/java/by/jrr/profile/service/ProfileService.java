package by.jrr.profile.service;

import by.jrr.auth.bean.User;
import by.jrr.auth.service.UserSearchService;
import by.jrr.auth.service.UserService;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    private final Supplier<Integer> DEFAULT_PAGE_NUMBER = () -> 1;
    private final Supplier<Integer> DEFAULT_ELEMENTS_PER_PAGE = () -> 15;

    @Autowired
    UserService userService;
    @Autowired
    UserSearchService userSearchService;
    @Autowired
    ProfileRepository profileRepository;

    public Page<Profile> findAllProfilesPageable(Optional<Integer> userFriendlyNumberOfPage,
                                                 Optional<Integer> numberOfElementsPerPage,
                                                 Optional<String> searchTerm) {
        // pages are begins from 0, but userFriendly is to begin from 1
        int page = userFriendlyNumberOfPage.orElseGet(DEFAULT_PAGE_NUMBER) - 1;
        int elem = numberOfElementsPerPage.orElseGet(DEFAULT_ELEMENTS_PER_PAGE);
        if(searchTerm.isPresent()) {
            List<User> userList = searchUsersByAnyUserField(searchTerm.get());
            if (userList.size()!= 0) {
                Iterable<Long> ids = userList.stream().map(User::getId).collect(Collectors.toList());
                List<Profile> profiles = profileRepository.findAllByUserIdIn(ids);

                // TODO: 26/05/20 this pagination should be moved in a static method
                Pageable pageable = PageRequest.of(page, elem);
                int pageOffset = (int) pageable.getOffset(); // TODO: 26/05/20 dangerous cast!
                int toIndex = (pageOffset + elem) > profiles.size() ? profiles.size() : pageOffset + elem;
                Page<Profile> profilePageImpl  = new PageImpl<>(profiles.subList(pageOffset, toIndex), pageable, profiles.size());
                profiles.forEach(profile -> setUserDataToProfile(profile));
                return profilePageImpl;
            }
        }

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

    private List<User> searchUsersByAnyUserField(String searchTerm) {
        return userSearchService.searchUserByAllUserFields(searchTerm);
    }
}

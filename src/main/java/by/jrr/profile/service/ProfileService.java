package by.jrr.profile.service;

import by.jrr.auth.bean.User;
import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.service.UserSearchService;
import by.jrr.auth.service.UserService;
import by.jrr.feedback.bean.EntityType;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.bean.StreamAndTeamSubscriber;
import by.jrr.profile.bean.SubscriptionStatus;
import by.jrr.profile.bean.UserProfileStatisticDTO;
import by.jrr.profile.repository.ProfileRepository;
import by.jrr.registration.bean.EventType;
import by.jrr.registration.bean.StudentActionToLog;
import by.jrr.registration.service.StudentActionToLogService;
import org.hibernate.mapping.Collection;
import org.jsoup.select.Collector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

// TODO: 24/06/20 got stack overFlow once here on setUserDataToProfile when I set profiles subscribers / subscriptions. Consider to make it clear and simple

@Service
public class ProfileService {
    private final Supplier<Integer> DEFAULT_PAGE_NUMBER = () -> 1;
    private final Supplier<Integer> DEFAULT_ELEMENTS_PER_PAGE = () -> 30;

    @Autowired
    UserService userService;
    @Autowired
    UserSearchService userSearchService;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    StreamAndTeamSubscriberService streamAndTeamSubscriberService;
    @Autowired
    ProfilePossessesService pss;
    @Autowired
    StudentActionToLogService satls;

    public Page<Profile> findAllProfilesPageable(Optional<Integer> userFriendlyNumberOfPage,
                                                 Optional<Integer> numberOfElementsPerPage,
                                                 Optional<String> searchTerm) {
        // pages are begins from 0, but userFriendly is to begin from 1
        int page = userFriendlyNumberOfPage.orElseGet(DEFAULT_PAGE_NUMBER) - 1;
        int elem = numberOfElementsPerPage.orElseGet(DEFAULT_ELEMENTS_PER_PAGE);
        if (searchTerm.isPresent()) {
            List<User> userList = searchUsersByAnyUserField(searchTerm.get());
            if (userList.size() != 0) {
                Iterable<Long> ids = userList.stream().map(User::getId).collect(Collectors.toList());
                List<Profile> profiles = profileRepository.findAllByUserIdIn(ids);

                // TODO: 26/05/20 this pagination should be moved in a static method
                Pageable pageable = PageRequest.of(page, elem);
                int pageOffset = (int) pageable.getOffset(); // TODO: 26/05/20 dangerous cast!
                int toIndex = (pageOffset + elem) > profiles.size() ? profiles.size() : pageOffset + elem;
                Page<Profile> profilePageImpl = new PageImpl<>(profiles.subList(pageOffset, toIndex), pageable, profiles.size());
                profiles.forEach(profile -> setUserDataToProfile(profile));
                return profilePageImpl;
            }
        }

        Page<Profile> profilePage = profileRepository.findAll(PageRequest.of(page, elem)); // TODO: 26/05/20 test for NPE
        profilePage.forEach(a -> setUserDataToProfile(a));
        return profilePage;
    }

    private void setUserDataToProfile(Profile profile) {
        userService.findUserById(profile.getUserId()).ifPresent(user -> profile.setUser(user));

        if (profile.getUser().hasRole(UserRoles.ROLE_STREAM) // TODO: 07/06/20 split to setProfileData & setSubscribers
                || profile.getUser().hasRole(UserRoles.ROLE_TEAM)) {
            List<StreamAndTeamSubscriber> subscribers = streamAndTeamSubscriberService.getAllSubscribersForProfileByProfileId(profile.getId());

            // TODO: 24/06/20 what happens here? refactor: set subscribers to streamTeam
            for (StreamAndTeamSubscriber subscriber : subscribers) {
                Optional<Profile> optionalProfile = this.findProfileByProfileId(subscriber.getSubscriberProfileId());
                if (optionalProfile.isPresent()) {
                    subscriber.setSubscriberProfile(optionalProfile.get()); // TODO: 07/06/20 consider refactoring to Java8 style
                }
            }
            profile.setSubscribers(subscribers);
        } else {
            List<StreamAndTeamSubscriber> subscriptions = streamAndTeamSubscriberService.getAllSubscriptionsForProfileByProfileId(profile.getId());
            // TODO: 24/06/20 what happens here? refactor: set subscriptions to user profile
            for (StreamAndTeamSubscriber subscription : subscriptions) {
                Optional<Profile> optionalProfile = this.findProfileByProfileIdLazy(subscription.getStreamTeamProfileId());
                if (optionalProfile.isPresent()) {
                    subscription.setSubscriptionProfile(optionalProfile.get()); // TODO: 07/06/20 consider refactoring to Java8 style
                    subscription.getSubscriptionProfile().setUser(userService.findUserById(subscription.getSubscriptionProfile().getUserId()).get()); // TODO: 24/06/20 get? consider to handle null
                }
            }
            profile.setSubscriptions(subscriptions);
        }
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

    public Optional<Profile> findProfileByProfileId(Long id) {
        if (id == null) {
            return Optional.empty();
            // TODO: 17/06/20 поймал ошибку на CI, что из Бд пришел null.
            // возможно, что из-за того, что руками удалял поля с any-to-many//
            // TODO: 17/06/20 детально залогировать, потому что ложит вьюху

        }
        Optional<Profile> profile = profileRepository.findById(id);
        profile.ifPresent(p -> setUserDataToProfile(p));
        return profile;
    }

    public Optional<Profile> findProfileByProfileIdLazy(Long id) {
        if (id == null) {
            return Optional.empty();
            // TODO: 17/06/20 поймал ошибку на CI, что из Бд пришел null.
            // возможно, что из-за того, что руками удалял поля с any-to-many//
            // TODO: 17/06/20 детально залогировать, потому что ложит вьюху

        }
        Optional<Profile> profile = profileRepository.findById(id);
        return profile;
    }

    public Profile getCurrentUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        Profile profile = profileRepository.findByUserId(user.getId()).orElseGet(() -> createAndSaveProfileForUser(user));
        profile.setUser(user);
        profile.setSubscriptions(this.streamAndTeamSubscriberService.getAllSubscriptionsForProfileByProfileId(profile.getId()));
        return profile;
    }

    public Long getCurrentUserProfileId() {
        return getCurrentUserProfile().getId();
    }

    public Profile createProfile(Profile profile) {
        Profile savedProfile = profileRepository.save(profile);
        pss.savePossessForCurrentUser(savedProfile.getId(), EntityType.PROFILE);
        return profileRepository.save(profile);
    }

    public Profile updateProfile(Profile profile) {
        //user could update his own profile or profile stream/team he possess
        if (profile.getId().equals(this.getCurrentUserProfileId())
                || pss.isCurrentUserOwner(profile.getId())) {
            profile = profileRepository.save(profile);
        }
        return profile;
    }


    // TODO: 07/06/20 выглядит стремно... возможно, если его дернуть не из того места. будет неожиданное поведение. Облажить тестами.
    // TODO: 07/06/20 возможно, нужно проверять собственника только если юзер тим или стрим. В остальных случаях считать собственником по профиль айди.
    // TODO: 07/06/20 тогда даже если в этом месте косяк, секьюрность не афектнет
    // TODO: 07/06/20 В любом профиле собственник = тот, чей это профиль, кроме стрим или группа.
    // TODO: 07/06/20 Cito! не работает это, NPE. Consider how to set profile Owner?
    public Profile createAndSaveProfileForUser(User user, Long courseId) { // creates profile for stream (of team?)
        Profile profile = profileRepository
                .save(Profile.builder()
                        .userId(user.getId())
                        .courseId(courseId)
                        .ownerProfileId(getCurrentUserProfile().getId())
                        .build());
        // set profile owner
//        if (profile.getUser().getRoles().contains(UserRoles.TEAM)
//                || profile.getUser().getRoles().contains(UserRoles.STREAM)) {
//            profile.setOwnerProfileId(getCurrentUserProfile().getId());
//        } else {
//            profile.setOwnerProfileId(profile.getId());
//        }
        return createProfile(profile);
    }

    public Profile createAndSaveProfileForUser(User user) { //creates profile for user
        Profile profile = profileRepository
                .save(
                        Profile
                                .builder()
                                .userId(
                                        user
                                                .getId())
                                .build());
        // set profile owner
//        if (profile.getUser().getRoles().contains(UserRoles.TEAM)
//                || profile.getUser().getRoles().contains(UserRoles.STREAM)) {
//            profile.setOwnerProfileId(getCurrentUserProfile().getId());
//        } else {
//            profile.setOwnerProfileId(profile.getId());
//        }
        return createProfile(profile);
    }

    public void enrollToStreamTeamProfile(Long streamTeamProfileId, Long subscriberProfileId) {


        streamAndTeamSubscriberService.updateSubscription(streamTeamProfileId,
                subscriberProfileId,
                SubscriptionStatus.REQUESTED);
    }

    private List<User> searchUsersByAnyUserField(String searchTerm) {
        return userSearchService.searchUserByAllUserFields(searchTerm);
    }

    public Optional<Profile> findNearestFromNowOpennForEnrolStreamByCourseId(Long courseId) {
        try {
            return profileRepository.findAllByCourseIdAndDateStartAfter(courseId, LocalDate.now()).stream()
                    .filter(p -> p.getDateStart() != null)
                    .min(Comparator.comparing(p -> p.getDateStart()));
        } catch (Exception ex) {
            // TODO: 16/06/20 log exception
            System.out.println("exception on finding man date of course");
            return Optional.empty();
        }
    }

    public List<Profile> findStreamsByCourseIdFromNowAndLastMonth(Long courseId) {
        List<Profile> streams = new ArrayList<>();
        try {
            streams = profileRepository.findAllByCourseIdAndDateStartAfter(courseId, LocalDate.now().minusMonths(1)).stream()
                    .filter(p -> p.getDateStart() != null)
                    .sorted(Comparator.comparing(p -> p.getDateStart()))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            // TODO: 16/06/20 log exception
            System.out.println("exception on finding man date of course");
            return streams;
        }
        return streams;
    }

    public UserProfileStatisticDTO caclulateStatisticsForUserProfile(StreamAndTeamSubscriber subscriber) { // TODO: 27/07/20 move this method to salts as statistics
        List<StudentActionToLog> userActionsLog = satls.findAllActionsForProfileId(subscriber.getSubscriberProfileId());
        UserProfileStatisticDTO userProfileStatisticDTO = new UserProfileStatisticDTO();
        setFirstLecture(userActionsLog, userProfileStatisticDTO);
        setLectures(userActionsLog, userProfileStatisticDTO);
        setFeedbacks(userActionsLog, userProfileStatisticDTO);
        setTeamStandups(userActionsLog, userProfileStatisticDTO);
        setTelegrams(userActionsLog, userProfileStatisticDTO);
        userProfileStatisticDTO.setUserFirstAndLastName(subscriber.getFullSubscriberName());
        return userProfileStatisticDTO;
    }

    private void setFirstLecture(List<StudentActionToLog> userActionsLog, UserProfileStatisticDTO userProfileStatisticDTO) {
        List<StudentActionToLog> firstLection = userActionsLog.stream()
//                .peek(e -> System.out.println("before filter date is after: " + e))
                .filter(a -> a.getTimestamp().isAfter(LocalDateTime.of(2020, 07, 06, 18, 30)))
//                .peek(e -> System.out.println("after filter date is after: " + e))
                .filter(a -> a.getTimestamp().isBefore(LocalDateTime.of(2020, 07, 06, 19, 30)))
//                .peek(e -> System.out.println("after filter date is Before: " + e))
                .collect(Collectors.toList());
        if (firstLection.size() > 0) {
            userProfileStatisticDTO.getLectures().add("Lecture 1");
        }
    }

    private void setLectures(List<StudentActionToLog> userActionsLog, UserProfileStatisticDTO userProfileStatisticDTO) {
        List<String> lectures = userActionsLog.stream()
                .filter(a -> a.getEventType().equals(EventType.TEAM_STAND_UP))
                .filter(a -> a.getEventName().toLowerCase().startsWith("lecture"))
                .map(StudentActionToLog::getEventName)
                .distinct()
                .collect(Collectors.toList());
        userProfileStatisticDTO.getLectures().addAll(lectures);
    }

    private void setFeedbacks(List<StudentActionToLog> userActionsLog, UserProfileStatisticDTO userProfileStatisticDTO) {
        List<String> feedbacks = userActionsLog.stream()
                .filter(a -> a.getEventType().equals(EventType.FEEDBACK))
                .map(StudentActionToLog::getEventName)
                .distinct()
                .collect(Collectors.toList());
        userProfileStatisticDTO.getFeedbacks().addAll(feedbacks);
    }

    private void setTeamStandups(List<StudentActionToLog> userActionsLog, UserProfileStatisticDTO userProfileStatisticDTO) {
        List<LocalDate> standups = userActionsLog.stream()
                .filter(a -> a.getEventType().equals(EventType.TEAM_STAND_UP))
                .filter(a -> a.getTimestamp().isAfter(LocalDateTime.of(2020, 07, 06, 23, 59)))
                .filter(a -> !a.getEventName().toLowerCase().startsWith("lecture"))
                .map(a -> LocalDate.of(a.getTimestamp().getYear(), a.getTimestamp().getMonth(), a.getTimestamp().getDayOfMonth()))
                .distinct()
                .collect(Collectors.toList());
        userProfileStatisticDTO.getStandups().addAll(standups);
    }

    private void setTelegrams(List<StudentActionToLog> userActionsLog, UserProfileStatisticDTO userProfileStatisticDTO) {
        List<LocalDate> telegrams = userActionsLog.stream()
                .filter(a -> a.getEventType().equals(EventType.TELEGRAM_CHAT))
                .filter(a -> a.getTimestamp().isAfter(LocalDateTime.of(2020, 07, 06, 23, 59)))
                .map(a -> LocalDate.of(a.getTimestamp().getYear(), a.getTimestamp().getMonth(), a.getTimestamp().getDayOfMonth()))
                .distinct()
                .collect(Collectors.toList());
        userProfileStatisticDTO.getTelegram().addAll(telegrams);
    }

}

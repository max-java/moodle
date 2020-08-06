package by.jrr.profile.service;

import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.service.UserAccessService;
import by.jrr.feedback.bean.EntityType;
import by.jrr.moodle.bean.CourseToLecture;
import by.jrr.moodle.bean.Lecture;
import by.jrr.moodle.bean.PracticeQuestion;
import by.jrr.moodle.service.CourseToLectureService;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.bean.ProfilePossesses;
import by.jrr.profile.bean.StreamAndTeamSubscriber;
import by.jrr.profile.bean.SubscriptionStatus;
import by.jrr.profile.repository.ProfilePossessesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfilePossessesService {

    @Autowired
    ProfilePossessesRepository profilePossessesRepository;

    @Autowired
    ProfileService profileService;
    @Autowired
    CourseToLectureService courseToLectureService;
    @Autowired
    UserAccessService userAccessService;

    public boolean isUserOwner(Long profleId, Long entityId) {
        Optional<ProfilePossesses> possess = profilePossessesRepository.findByProfileIdAndEntityId(profleId, entityId);
        return possess.isPresent();
    }
    public boolean isCurrentUserOwner(Long entityId) {
        if(userAccessService.isCurrentUserAuthenticated()) {
            Optional<ProfilePossesses> possess =
                    profilePossessesRepository.findByProfileIdAndEntityId(profileService.getCurrentUserProfile().getId(), entityId);
            return possess.isPresent();
        }
        return false;// TODO: 08/07/20 add log here with info of response user got for entity ID

    }

    public void savePossessForCurrentUser(Long entityId, EntityType type) {

            ProfilePossesses possess = ProfilePossesses.builder()
                    .profileId(profileService.getCurrentUserProfile().getId())
                    .entityId(entityId)
                    .entityType(type)
                    .build();
        try {
            profilePossessesRepository.save(possess);
        } catch (Exception ex) {
            // TODO: 12/06/20 log exception: possible id duplicates in unique fields.
        }
    }

    public boolean isUserHasAccessToReadProfile(Profile profile) { // TODO: 16/06/20 replace predicates with check methods
        if(!profile.getUser().hasRole(UserRoles.ROLE_TEAM)
                || !profile.getUser().hasRole(UserRoles.ROLE_STREAM)) {
            return true;
        }
        if(profileService.getCurrentUserProfile().getId().equals(profile.getOwnerProfileId())) {
            return true;
        }
        if(profile.getSubscribers()
                .stream().anyMatch(sub -> sub.getSubscriberProfileId().equals(profileService.getCurrentUserProfile().getId()))) {
            return true;
        }
        return false;
    }

    public boolean isUserHasAccessToLecture(Lecture lecture) { // TODO: 24/06/20 this should be cached as List of lectures that user has access to and moved into userAccessService

        //user could have access only to subscribed and approved course lectures
        Profile profile = profileService.getCurrentUserProfile();
        List<StreamAndTeamSubscriber> approvedSubscriptions = profile.getSubscriptions().stream()
                .filter(s -> s.getStatus().equals(SubscriptionStatus.APPROVED))
                .collect(Collectors.toList());
        List<Long> coursesId = approvedSubscriptions.stream()
                .map(s -> s.getSubscriptionProfile().getCourseId())
                .collect(Collectors.toList());
        List<Lecture> lecturesThatUserHasAccessTo = coursesId.stream()
                .flatMap(a -> courseToLectureService.findLecturesForCourse(a, null).stream())
                .collect(Collectors.toList());
        return lecturesThatUserHasAccessTo.contains(lecture);


    }
    public boolean isUserHasAccessToPractice(PracticeQuestion practiceQuestion) { // TODO: 24/06/20 this should be cached as List of lectures that user has access to and moved into userAccessService

        //user could have access only to subscribed and approved course lectures and practice in course
        Profile profile = profileService.getCurrentUserProfile();

        List<StreamAndTeamSubscriber> approvedSubscriptions = profile.getSubscriptions().stream()
                .filter(s -> s.getStatus().equals(SubscriptionStatus.APPROVED))
                .collect(Collectors.toList());
        List<Long> coursesId = approvedSubscriptions.stream()
                .map(s -> s.getSubscriptionProfile().getCourseId())
                .collect(Collectors.toList());
        List<Lecture> lecturesThatUserHasAccessTo = coursesId.stream()
                .flatMap(a -> courseToLectureService.findLecturesForCourse(a, null).stream())
                .collect(Collectors.toList());
        List<PracticeQuestion> practiceQuestionsUserHasAccessTo = lecturesThatUserHasAccessTo.stream()
                .flatMap(lecture -> lecture.getPracticeQuestions().stream())
                .collect(Collectors.toList());
        List<Long> idsOfpracticeQuestionsUserHasAccessTo = practiceQuestionsUserHasAccessTo.stream()
                .map(qa -> qa.getId())
                .collect(Collectors.toList());


        return idsOfpracticeQuestionsUserHasAccessTo.contains(practiceQuestion.getId());


    }
}

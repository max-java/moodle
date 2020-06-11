package by.jrr.profile.service;

import by.jrr.auth.service.UserSearchService;
import by.jrr.auth.service.UserService;
import by.jrr.feedback.bean.EntityType;
import by.jrr.feedback.bean.ReviewRequest;
import by.jrr.feedback.bean.ReviewResult;
import by.jrr.feedback.bean.Reviewable;
import by.jrr.feedback.service.FeedbackService;
import by.jrr.moodle.bean.Lecture;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.bean.ProfileStatistics;
import by.jrr.profile.repository.ProfileRepository;
import by.jrr.project.bean.Issue;
import by.jrr.project.bean.IssueType;
import by.jrr.statistic.bean.TrackStatus;
import by.jrr.statistic.bean.Trackable;
import by.jrr.statistic.bean.UserProgress;
import by.jrr.statistic.service.UserProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


// TODO: 05/06/20 revise all logic

@Service
public class ProfileStatisticService {

    @Autowired
    UserService userService;
    @Autowired
    UserSearchService userSearchService;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    UserProgressService userProgressService;
    @Autowired
    FeedbackService feedbackService;

    public ProfileStatistics calculateStatisticsForProfile(Long profileId) {
        ProfileStatistics ps = new ProfileStatistics();
        setTrackable(ps, profileId);
        setReviewable(ps, profileId);
        return ps;
    }

    private void setTrackable(ProfileStatistics ps, Long profileId) {
        Map<EntityType, List<UserProgress>> trackables = this.getAllTrackableForUserGroupByTrackableType(profileId);
        setLecturesLearnedAsTrackable(ps, trackables);
        setLecturesReadAsTrackable(ps, trackables);
        setTopicsLearnedAsTrackable(ps, trackables);
        setTopicsReadAsTrackable(ps, trackables);
        setQAndAsLearnedAsTrackable(ps, trackables);
        setQAndAsReadAsTrackable(ps, trackables);
    }

    private void setReviewable(ProfileStatistics ps, Long profileId) {
        Map<EntityType, List<ReviewRequest>> reviewables = this.getAllReviewableForUserGroupByReviewdItemTypeType(profileId);
        setPracticesSubmittedAsReviewable(ps, reviewables);
        setPracticesApprovedAsReviewable(ps, reviewables);
        setPracticesRejectedAsReviewable(ps, reviewables);
        setIssuesSubmittedAsReviewable(ps, reviewables);
        setIssuesApprovedAsReviewable(ps, reviewables);
        setIssuesRejectedAsReviewable(ps, reviewables);
        setSubjectsSubmittedAsReviewable(ps, reviewables);
        setSubjectsApprovedAsReviewable(ps, reviewables);
        setSubjectsRejectedAsReviewable(ps, reviewables);
    }

    private Map<EntityType, List<UserProgress>> getAllTrackableForUserGroupByTrackableType(Long profileId) { // TODO: 05/06/20 replace by Enum map
        List<UserProgress> userProgressList = userProgressService.getAllUserProgressByProfileIdEager(profileId);
        Map<EntityType, List<UserProgress>> allTrackabe = userProgressList.stream()
                .collect(Collectors.groupingBy(UserProgress::getTrackableType, Collectors.mapping(Function.identity(), Collectors.toList())));
        return allTrackabe;
    }

    private Map<EntityType, List<ReviewRequest>> getAllReviewableForUserGroupByReviewdItemTypeType(Long profileId) { // TODO: 05/06/20 replace by Enum map
        List<ReviewRequest> userReviewRequestList = feedbackService.fingAllReviewRequestForUser(profileId);
        Map<EntityType, List<ReviewRequest>> allReviewable = userReviewRequestList.stream()
                .collect(Collectors.groupingBy(a -> a.getItem().getReviewedItemType(), Collectors.mapping(Function.identity(), Collectors.toList())));
        return allReviewable;
    }

    private void setLecturesLearnedAsTrackable(ProfileStatistics ps, Map<EntityType, List<UserProgress>> trackables) {
        ps.setLecturesLearnedT(trackables.getOrDefault(EntityType.LECTURE, new LinkedList<>())
                .stream()
                .filter(s -> s.getTrackStatus().equals(TrackStatus.LEARNED))
                .map(s -> s.getTrackable())
                .collect(Collectors.toList()));
    }

    private void setLecturesReadAsTrackable(ProfileStatistics ps, Map<EntityType, List<UserProgress>> trackables) {
        ps.setLecturesReadT(trackables.getOrDefault(EntityType.LECTURE, new LinkedList<>())
                .stream()
                .filter(s -> s.getTrackStatus().equals(TrackStatus.LEARNED))
                .map(s -> s.getTrackable())
                .collect(Collectors.toList()));
    }

    private void setTopicsLearnedAsTrackable(ProfileStatistics ps, Map<EntityType, List<UserProgress>> trackables) {
        ps.setTopicsLearnedT(trackables.getOrDefault(EntityType.TOPIC, new LinkedList<>())
                .stream()
                .filter(s -> s.getTrackStatus().equals(TrackStatus.LEARNED))
                .map(s -> s.getTrackable())
                .collect(Collectors.toList()));
    }

    private void setTopicsReadAsTrackable(ProfileStatistics ps, Map<EntityType, List<UserProgress>> trackables) {
        ps.setTopicsReadT(trackables.getOrDefault(EntityType.TOPIC, new LinkedList<>())
                .stream()
                .filter(s -> s.getTrackStatus().equals(TrackStatus.READ))
                .map(s -> s.getTrackable())
                .collect(Collectors.toList()));
    }

    private void setQAndAsLearnedAsTrackable(ProfileStatistics ps, Map<EntityType, List<UserProgress>> trackables) {
        ps.setQAndAsLearnedT(trackables.getOrDefault(EntityType.INTERVIEW_QUESTION, new LinkedList<>())
                .stream()
                .filter(s -> s.getTrackStatus().equals(TrackStatus.LEARNED))
                .map(s -> s.getTrackable())
                .collect(Collectors.toList()));
    }

    private void setQAndAsReadAsTrackable(ProfileStatistics ps, Map<EntityType, List<UserProgress>> trackables) {
        ps.setQAndAsReadT(trackables.getOrDefault(EntityType.INTERVIEW_QUESTION, new LinkedList<>())
                .stream()
                .filter(s -> s.getTrackStatus().equals(TrackStatus.READ))
                .map(s -> s.getTrackable())
                .collect(Collectors.toList()));
    }

    private void setPracticesSubmittedAsReviewable(ProfileStatistics ps, Map<EntityType, List<ReviewRequest>> reviewables) {
        ps.setPracticesSubmittedR(reviewables.getOrDefault(EntityType.PRACTICE_QUESTION, new LinkedList<>())
                .stream()
                .filter(s -> s.getReviewResultOnClosing().equals(ReviewResult.NONE))
                .map(s -> s.getItem().getReviewedEntity())
                .collect(Collectors.toList()));
    }

    private void setPracticesApprovedAsReviewable(ProfileStatistics ps, Map<EntityType, List<ReviewRequest>> reviewables) {
        ps.setPracticesApprovedR(reviewables.getOrDefault(EntityType.PRACTICE_QUESTION, new LinkedList<>())
                .stream()
                .filter(s -> s.getReviewResultOnClosing().equals(ReviewResult.APPROVE))
                .map(s -> s.getItem().getReviewedEntity())
                .collect(Collectors.toList()));
    }

    private void setPracticesRejectedAsReviewable(ProfileStatistics ps, Map<EntityType, List<ReviewRequest>> reviewables) {
        ps.setPracticesRejectedR(reviewables.getOrDefault(EntityType.PRACTICE_QUESTION, new LinkedList<>())
                .stream()
                .filter(s -> s.getReviewResultOnClosing().equals(ReviewResult.REQUEST_CHANGES))
                .map(s -> s.getItem().getReviewedEntity())
                .collect(Collectors.toList()));
    }

    private void setIssuesSubmittedAsReviewable(ProfileStatistics ps, Map<EntityType, List<ReviewRequest>> reviewables) {
        ps.setIssuesCRSubmittedR(reviewables.getOrDefault(EntityType.ISSUE, new LinkedList<>())
                .stream()
                .filter(s -> s.getReviewResultOnClosing().equals(ReviewResult.NONE))
                .map(s -> s.getItem().getReviewedEntity())
                .collect(Collectors.toList()));
    }

    private void setIssuesApprovedAsReviewable(ProfileStatistics ps, Map<EntityType, List<ReviewRequest>> reviewables) {
        List<Reviewable> issuesCRApproved = reviewables.getOrDefault(EntityType.ISSUE, new LinkedList<>())
                .stream()
                .filter(s -> s.getReviewResultOnClosing().equals(ReviewResult.APPROVE))
                .map(s -> s.getItem().getReviewedEntity())
                .collect(Collectors.toList());

        List<Issue> issues = new LinkedList<>();
        for(Reviewable reviewable : issuesCRApproved) {
            try{
                issues.add((Issue) reviewable);
            } catch (Exception ex) {
                ex.printStackTrace();
                // TODO: 06/06/20 log it
            }
        }

        ps.setIssuesCRApprovedR(issuesCRApproved);
        ps.setIssuesCRApprovedBugsR(issues.stream()
                .filter(i -> i.getIssueType().equals(IssueType.BUG))
                .collect(Collectors.toList()));
        ps.setIssuesCRApprovedStoriesR(issues.stream()
                .filter(i -> i.getIssueType().equals(IssueType.STORY))
                .collect(Collectors.toList()));
    }

    private void setIssuesRejectedAsReviewable(ProfileStatistics ps, Map<EntityType, List<ReviewRequest>> reviewables) {
        ps.setIssuesCRRejectedR(reviewables.getOrDefault(EntityType.ISSUE, new LinkedList<>())
                .stream()
                .filter(s -> s.getReviewResultOnClosing().equals(ReviewResult.REQUEST_CHANGES))
                .map(s -> s.getItem().getReviewedEntity())
                .collect(Collectors.toList()));
    }

    private void setSubjectsSubmittedAsReviewable(ProfileStatistics ps, Map<EntityType, List<ReviewRequest>> reviewables) {
        ps.setSubjectsCRSubmittedR(reviewables.getOrDefault(EntityType.SUBJECT, new LinkedList<>())
                .stream()
                .filter(s -> s.getReviewResultOnClosing().equals(ReviewResult.NONE))
                .map(s -> s.getItem().getReviewedEntity())
                .collect(Collectors.toList()));
    }

    private void setSubjectsApprovedAsReviewable(ProfileStatistics ps, Map<EntityType, List<ReviewRequest>> reviewables) {
        ps.setSubjectsCRApprovedR(reviewables.getOrDefault(EntityType.SUBJECT, new LinkedList<>())
                .stream()
                .filter(s -> s.getReviewResultOnClosing().equals(ReviewResult.APPROVE))
                .map(s -> s.getItem().getReviewedEntity())
                .collect(Collectors.toList()));
    }

    private void setSubjectsRejectedAsReviewable(ProfileStatistics ps, Map<EntityType, List<ReviewRequest>> reviewables) {
        ps.setSubjectsCRRejectedR(reviewables.getOrDefault(EntityType.SUBJECT, new LinkedList<>())
                .stream()
                .filter(s -> s.getReviewResultOnClosing().equals(ReviewResult.REQUEST_CHANGES))
                .map(s -> s.getItem().getReviewedEntity())
                .collect(Collectors.toList()));
    }

}

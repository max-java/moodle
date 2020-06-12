package by.jrr.profile.bean;

import by.jrr.feedback.bean.Reviewable;
import by.jrr.interview.bean.QAndA;
import by.jrr.moodle.bean.Lecture;
import by.jrr.moodle.bean.PracticeQuestion;
import by.jrr.moodle.bean.Topic;
import by.jrr.portfolio.bean.Subject;
import by.jrr.project.bean.Issue;
import by.jrr.statistic.bean.Trackable;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class ProfileStatistics {

    private List<Lecture> lecturesRead = new LinkedList<>();
    private List<Lecture> lecturesLearned = new LinkedList<>();

    private List<Topic> topicsRead = new LinkedList<>();
    private List<Topic> topicsLearned = new LinkedList<>();

    private List<QAndA> qAndAsRead = new LinkedList<>();
    private List<QAndA> qAndAsLearned = new LinkedList<>();

    private List<PracticeQuestion> practicesSubmitted = new LinkedList<>();
    private List<PracticeQuestion> practicesApproved = new LinkedList<>();
    private List<PracticeQuestion> practicesRejected = new LinkedList<>();

    private List<Issue> issuesCRSubmitted = new LinkedList<>();
    private List<Issue> issuesCRApproved = new LinkedList<>();
    private List<Issue> issuesCRRejected = new LinkedList<>();

    private List<Subject> subjectsCRSubmitted = new LinkedList<>();
    private List<Subject> subjectsCRApproved = new LinkedList<>();
    private List<Subject> subjectsCRRejected = new LinkedList<>();


    private List<Trackable> lecturesReadT = new LinkedList<>();
    private List<Trackable> lecturesLearnedT = new LinkedList<>();

    private List<Trackable> topicsReadT = new LinkedList<>();
    private List<Trackable> topicsLearnedT = new LinkedList<>();

    private List<Trackable> qAndAsReadT = new LinkedList<>();
    private List<Trackable> qAndAsLearnedT = new LinkedList<>();

    private List<Reviewable> practicesSubmittedR = new LinkedList<>();
    private List<Reviewable> practicesApprovedR = new LinkedList<>();
    private List<Reviewable> practicesRejectedR = new LinkedList<>();

    private List<Reviewable> issuesCRSubmittedR = new LinkedList<>();
    private List<Reviewable> issuesCRApprovedR = new LinkedList<>();
    private List<Reviewable> issuesCRApprovedStoriesR = new LinkedList<>();
    private List<Reviewable> issuesCRApprovedBugsR = new LinkedList<>();
    private List<Reviewable> issuesCRRejectedR = new LinkedList<>();

    private List<Reviewable> subjectsCRSubmittedR = new LinkedList<>();
    private List<Reviewable> subjectsCRApprovedR = new LinkedList<>();
    private List<Reviewable> subjectsCRRejectedR = new LinkedList<>();

}

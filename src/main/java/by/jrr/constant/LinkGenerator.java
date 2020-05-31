package by.jrr.constant;

import by.jrr.moodle.bean.Course;
import by.jrr.moodle.bean.Lecture;
import by.jrr.moodle.bean.PracticeQuestion;
import by.jrr.moodle.bean.Topic;
import by.jrr.profile.bean.Profile;
import by.jrr.project.bean.Issue;

public class LinkGenerator {

    public static String getLinkTo(Object o) {
        if (o instanceof Issue) {
            Issue issue = (Issue) o;
            return Endpoint.PROJECT + "/" + issue.getProjectId() + Endpoint.ISSUE + "/" + issue.getIssueId();
        }
        if (o instanceof Profile) {
            Profile profile = (Profile) o;
            return Endpoint.PROFILE_CARD + "/" + profile.getId();
        }
        if (o instanceof Topic) {
            Topic topic = (Topic) o;
            return Endpoint.TOPIC + "/" + topic.getId();
        }
        if (o instanceof Lecture) {
            Lecture lecture = (Lecture) o;
            return Endpoint.LECTURE + "/" + lecture.getId();
        }
        if (o instanceof Course) {
            Course course = (Course) o;
            return Endpoint.COURSE + "/" + course.getId();
        }
        if (o instanceof PracticeQuestion) {
            PracticeQuestion practiceQuestion = (PracticeQuestion) o;
            return Endpoint.PRACTICE + "/" + practiceQuestion.getId();
        }

        return "#";
    }
}

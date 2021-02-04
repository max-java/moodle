package by.jrr.constant;

import by.jrr.files.bean.FileMeta;
import by.jrr.interview.bean.QAndA;
import by.jrr.library.bean.Book;
import by.jrr.moodle.bean.Course;
import by.jrr.moodle.bean.Lecture;
import by.jrr.moodle.bean.PracticeQuestion;
import by.jrr.moodle.bean.Topic;
import by.jrr.portfolio.bean.Domain;
import by.jrr.portfolio.bean.Subject;
import by.jrr.profile.bean.Profile;
import by.jrr.project.bean.Issue;

/**
 * This class is Used to simplify link creation in templates
 */

public class LinkGenerator {
    public static final String DEFAULT_USERPIC = "/dist/img/user5-128x128.jpg";

    public static String getLinkTo(Object o) {
        if (o instanceof Issue) {
            Issue issue = (Issue) o;
            return Endpoint.PROJECT + "/" + issue.getProjectId() + Endpoint.ISSUE + "/" + issue.getIssueId();
        }
        if (o instanceof Domain) {
            Domain domain = (Domain) o;
            return Endpoint.DOMAIN + "/" + domain.getId();
        }
        if (o instanceof Subject) {
            Subject subject = (Subject) o;
            return Endpoint.DOMAIN + "/" + subject.getDomainId() + Endpoint.SUBJECT + "/" + subject.getSubjectId();
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

        if (o instanceof Book) {
            Book book = (Book) o;
            return Endpoint.BOOK + "/" + book.getId();
        }

        if (o instanceof QAndA) {
            QAndA qAndA = (QAndA) o;
            return Endpoint.Q_AND_A + "/" + qAndA.getId();
        }
        //Files...
        if (o instanceof FileMeta) {
            FileMeta fileMeta = (FileMeta) o;
            if (fileMeta.getContentType().startsWith("image/")) {
                return Endpoint.IMAGE + "/" + fileMeta.getNameWithExtension();
            }
            if (fileMeta.getContentType().endsWith("/pdf")) {
                return Endpoint.PDF + "/" + fileMeta.getNameWithExtension();
            }
        }

        return "#";
    }

    public static String getLinkToUserpic(Object o) {
        if (o instanceof Profile) {
            Profile profile = (Profile) o;
            if(profile.getAvatarFileName() != null) {
                return Endpoint.IMAGE + "/" + profile.getAvatarFileName();
            } else {
                return DEFAULT_USERPIC;
            }
        }
        return "#";
    }

}

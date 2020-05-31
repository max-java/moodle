package by.jrr.constant;

import by.jrr.profile.bean.Profile;
import by.jrr.project.bean.Issue;

public class LinkGenerator {

    public static String getLinkTo(Object o) {
        if (o instanceof Issue) {
            Issue issue = (Issue) o;
            return Endpoint.PROJECT+"/"+issue.getProjectId()+Endpoint.ISSUE+"/"+issue.getIssueId();
        }
        if (o instanceof Profile) {
            Profile profile = (Profile) o;
            return Endpoint.PROFILE_CARD+"/"+profile.getId();
        }
        return "#";
    }
}

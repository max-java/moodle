package by.jrr.auth.bean;

public enum UserRoles {
    ADMIN("Has full access"),
    GUEST("Registered, but not participate in any courses"),
    FREE_STUDENT("Participate in any free course"),
    STUDENT("Participate in any paid course"),
    SCRUM_MASTER("Participate in course and playing scrum master role: could create a team"),
    ALUMNUS("Graduated from any paid courses"),
    STREAM("Group of a students led by trainer"),
    TEAM("Group of a students led by trainer led by scrum master");

    private String info;

    UserRoles(String info) {
        this.info = info;
    }
}

package by.jrr.auth.bean;

public enum UserRoles {
    ROLE_ADMIN("Has full access"),
    ROLE_GUEST("Registered, but not participate in any courses"),
    ROLE_FREE_STUDENT("Participate in any free course"),
    ROLE_STUDENT("Participate in any paid course"),
    ROLE_SCRUM_MASTER("Participate in course and playing scrum master role: could create a team"),
    ROLE_ALUMNUS("Graduated from any paid courses"),
    ROLE_STREAM("Group of a students led by trainer"),
    ROLE_TEAM("Group of a students led by trainer led by scrum master");

    private String info;

    UserRoles(String info) {
        this.info = info;
    }
}

//strong
//    ROLE_GUEST >> ROLE_FREE_STUDENT >> ROLE_STUDENT >> ROLE_SCRUM_MASTER >> ROLE_ALUMNUS >> ROLE_ADMIN >>

//scrum master should go separately.
//  >> ROLE_SCRUM_MASTER >>

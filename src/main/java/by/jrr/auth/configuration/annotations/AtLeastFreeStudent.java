package by.jrr.auth.configuration.annotations;

//import org.springframework.security.access.annotation.Secured;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
//@Secured({"ROLE_ADMIN", "ROLE_ALUMNUS", "ROLE_SCRUM_MASTER", "ROLE_STUDENT", "ROLE_FREE_STUDENT"})
public @interface AtLeastFreeStudent {
}
//ROLE_GUEST >> ROLE_FREE_STUDENT >> ROLE_STUDENT >> ROLE_SCRUM_MASTER >> ROLE_ALUMNUS >> ROLE_ADMIN >>

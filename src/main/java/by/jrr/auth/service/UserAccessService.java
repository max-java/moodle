package by.jrr.auth.service;

import by.jrr.auth.bean.User;
import by.jrr.auth.bean.UserData;
import by.jrr.auth.bean.UserRoles;
import by.jrr.profile.bean.Profile;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@Service
public class UserAccessService {



    public boolean isCurrentUserAuthenticated() {
        boolean isUserAuthenticated = false;
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                //when Anonymous Authentication is enabled
                && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            isUserAuthenticated = true;

        }
        return isUserAuthenticated;
    }

    public boolean isCurrentUserIsAdmin() {
        boolean isCurrentUserIsAdmin = false;
        if(isCurrentUserAuthenticated()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            isCurrentUserIsAdmin = authorities.stream().anyMatch(ga -> ga.getAuthority().equals(UserRoles.ROLE_ADMIN.name()));
        }
        return isCurrentUserIsAdmin;
    }

    public static  boolean hasRole (UserRoles role)
    { // TODO: 07/07/20 concider to make unified conception of access this functionality
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role.name()));
    }
    public boolean isUserhasRole (UserRoles role)
    {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role.name()));
    }
    public static boolean isUserHasRole(User user, UserRoles role) {
        return user.getAllRoles().contains(role.name());
    }
}
/*
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    Set<String> roles = authentication.getAuthorities().stream()
            .map(r -> r.getAuthority()).collect(Collectors.toSet());*/

/*

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    boolean hasUserRole = authentication.getAuthorities().stream()
            .anyMatch(r -> r.getAuthority().equals("ROLE_USER"));*/

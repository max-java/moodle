package by.jrr.auth.service;

import by.jrr.auth.bean.User;
import by.jrr.auth.bean.UserData;
import by.jrr.auth.bean.UserRoles;
import by.jrr.constant.Endpoint;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.bean.StreamAndTeamSubscriber;
import by.jrr.profile.bean.SubscriptionStatus;
import by.jrr.profile.bean.TimeLine;
import by.jrr.profile.service.ProfileService;
import by.jrr.profile.service.TimeLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAccessService {

    @Autowired
    ProfileService profileService;
    @Autowired
    TimeLineService timeLineService;

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

    public boolean isUserHasAccessToVideoFile(String fileDir, String fileName) {
        // TODO: 22/09/20 consider to syncronize it with timeline service policy links
        String link = String.format("/%s/%s", fileDir, fileName);
        Profile currentUserProfile = profileService.getCurrentUserProfile();
        List<StreamAndTeamSubscriber> subscriptions = currentUserProfile.getSubscriptions();

        List<Long> idStreamsUserHasAccess = subscriptions.stream()
                .filter(s -> s.getStatus().equals(SubscriptionStatus.APPROVED))
                .filter(s -> s.getStreamTeamProfileId() != null)
                .map(s -> s.getStreamTeamProfileId())
                .collect(Collectors.toList());

        List<TimeLine> items = idStreamsUserHasAccess.stream()
                .flatMap(id -> timeLineService.getTimelineByStreamId(id).stream())
                .collect(Collectors.toList());

        List<String> urlsToVideo = items.stream()
                .map(i -> i.getUrlToRedirect())
                .collect(Collectors.toList());

        boolean result = urlsToVideo.stream()
                .anyMatch(i -> i.equals(link));

        return result;
    }

    public boolean isUserHasAccessToSubcription(Long streamTeamProfileId) {
        Profile currentUserProfile = profileService.getCurrentUserProfile();
        List<StreamAndTeamSubscriber> subscriptions = currentUserProfile.getSubscriptions();
        return subscriptions.stream()
                .filter(s -> s.getStatus().equals(SubscriptionStatus.APPROVED))
                .anyMatch(s -> s.getStreamTeamProfileId().equals(streamTeamProfileId));
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

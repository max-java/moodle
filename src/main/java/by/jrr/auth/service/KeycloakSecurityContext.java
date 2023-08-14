//package by.jrr.auth.service;
//
//import by.jrr.auth.bean.User;
//import by.jrr.auth.bean.KeycloakUser;
//import org.keycloak.KeycloakPrincipal;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Optional;
//
//@Service
//public class KeycloakSecurityContext {
//
//    @Autowired
//    UserService userService;
//    @Autowired
//    HttpServletRequest request;
//
//    public Optional<KeycloakUser> getCurrentKeycloakUser() {
//        try {
//            var keycloakPrincipal = (KeycloakPrincipal) SecurityContextHolder
//                    .getContext()
//                    .getAuthentication()
//                    .getPrincipal();
//            var token = keycloakPrincipal.getKeycloakSecurityContext().getToken();
//            return Optional.of(new KeycloakUser(token.getSubject(), token.getPreferredUsername(), token.getEmail()));
//        } catch (Exception ex) {
//            return Optional.empty();
//        }
//    }
//
//    public Optional<User> getCurrentMoodleUserByEmail() {
//        if(getCurrentKeycloakUser().isPresent()) {
//            return Optional.ofNullable(userService.findUserByEmail(getCurrentKeycloakUser().get().getSsoEmail()));
//        }
//        return Optional.empty();
//    }
//
//    public Optional<KeycloakUser> getCurrentMoodleUserByUuid() {
//        if(getCurrentKeycloakUser().isPresent()) {
//            Optional<KeycloakUser> keycloakUser = userService.findUserByUuid(getCurrentKeycloakUser().get().getUuid());
//            if(keycloakUser.isPresent()) {
//                return keycloakUser;
//            } else {
//                Optional<User> user = getCurrentMoodleUserByEmail();
//                if(user.isPresent()) {
//                    KeycloakUser userWithTokenData = new KeycloakUser(user.get(),
//                            getCurrentKeycloakUser().get().getUuid(),
//                            getCurrentKeycloakUser().get().getPreferredUsername(),
//                            getCurrentKeycloakUser().get().getSsoEmail());
//                    return Optional.of(userService.saveKeycloakUser(userWithTokenData));
//                }
//            }
//        } else {
//            try {
//                request.logout();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//        return Optional.empty();
//    }
//
//    public Optional<User> getCurrentMoodleUser() {
//        Optional<KeycloakUser> keycloakUser = getCurrentMoodleUserByUuid();
//        if(keycloakUser.isPresent()) {
//            return Optional.of((User) keycloakUser.get());
//        }
//        return Optional.empty();
//    }
//}
//
////Migration
////
////INSERT INTO SSO.USER_ENTITY (ID, EMAIL, EMAIL_CONSTRAINT, EMAIL_VERIFIED, ENABLED, FEDERATION_LINK, FIRST_NAME, LAST_NAME, REALM_ID, USERNAME, CREATED_TIMESTAMP, SERVICE_ACCOUNT_CLIENT_LINK, NOT_BEFORE)
////        VALUES ('33e1c957-9885-4f5b-9d6e-eccc4d3a7f75', '6666350@gmail.com', '6666350@gmail.com', true, true, null, null, null, 'sso.tutrit.com', '6666350@gmail.com', 1619837216739, null, 0);
////INSERT INTO SSO.USER_ROLE_MAPPING (ROLE_ID, USER_ID) VALUES ('2ec5b346-fc26-417d-a43f-0973ce5452cb', '33e1c957-9885-4f5b-9d6e-eccc4d3a7f75');
////INSERT INTO SSO.USER_ROLE_MAPPING (ROLE_ID, USER_ID) VALUES ('3fcc41a2-414a-4015-83a7-f565dfc00cb1', '33e1c957-9885-4f5b-9d6e-eccc4d3a7f75');
////INSERT INTO SSO.USER_ROLE_MAPPING (ROLE_ID, USER_ID) VALUES ('7c3d8674-6a5b-48fc-a02a-3f1afb6d3d1a', '33e1c957-9885-4f5b-9d6e-eccc4d3a7f75');
////INSERT INTO SSO.USER_ROLE_MAPPING (ROLE_ID, USER_ID) VALUES ('c1e39e13-f4d5-4718-af81-55aeb379192e', '33e1c957-9885-4f5b-9d6e-eccc4d3a7f75');
//

package by.jrr.auth.service;

import by.jrr.api.model.UserContactsDto;
import by.jrr.auth.bean.KeycloakUser;
import by.jrr.auth.bean.Role;
import by.jrr.auth.bean.User;
import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.exceptios.UserNameConversionException;
import by.jrr.auth.exceptios.UserServiceException;
import by.jrr.auth.model.DropUserPassword;
import by.jrr.auth.repository.KeycloakUserRepository;
import by.jrr.auth.repository.RoleRepository;
import by.jrr.auth.repository.UserRepository;
import by.jrr.email.service.EMailService;
import by.jrr.crm.controller.admin.bean.UserDTO;
//import by.jrr.telegram.bot.service.MessageService;

import by.jrr.message.service.MessageService;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfileService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    public static final String USER_NOT_FOUND_EXCEPTION = "user with id %s not found";

    private KeycloakUserRepository keycloakUserRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    MessageService messageService;
    @Autowired
    ProfileService profileService;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       KeycloakUserRepository keycloakUserRepository) {
        this.keycloakUserRepository = keycloakUserRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User findUserByEmail(String email) {
//        return erasePasswordDataBeforeResponse(userRepository.findByEmail(email)); todo: delete user password data if necessary
        // TODO: 10/06/20 handle org.springframework.dao.IncorrectResultSizeDataAccessException:
        //  query did not return a unique result: 2;
        //  nested exception is javax.persistence.NonUniqueResultException: query did not return a unique result: 2
        // I use user email to register teams and streams, and registration is the same as for user. That is why
        // I've got several rows with the same emails.
        // when switching to keycloak, I need to bind keycloak users with moodle users, and I choose to do it by email.
        //
        try {
            return userRepository.findByEmail(email);
        } catch (Exception ex) {
            User user = findUserByUserName(email);
            if(user == null) {
                return userRepository.findFirstByEmailOrderByIdAsc(email);
            }
            return user;
        }
    }

    @Deprecated//use KeycloakSecurityContext
    public User findUserByUserName(String userName) {
//        return erasePasswordDataBeforeResponse(userRepository.findByUserName(userName)); todo: delete user password data if necessary
        return userRepository.findByUserName(userName);
    }

    /**
     * original place where new user has been created
     */
    public User saveUser(User user, Optional<UserRoles> userRoleOp) {
        UserRoles newUserRole = userRoleOp.orElseGet(() -> UserRoles.ROLE_GUEST);
        user.setActive(true);

        Role userRole = roleRepository.findByRole(newUserRole);
        if (userRole == null) { //fix to store new UserRoles ids based on new enum values
            userRole = roleRepository.save(new Role(null, newUserRole));
        }
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    //rename to findUserById(Long id) after clashes method will be deleted
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_EXCEPTION, id)));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll().stream()
                .collect(Collectors.toList());
    }

    public Optional<KeycloakUser> findUserByUuid(String uuid) {
        return keycloakUserRepository.findByUuid(uuid);
    }

    public KeycloakUser saveKeycloakUser(KeycloakUser keycloakUser) {
        return keycloakUserRepository.save(keycloakUser);
    }

    @Deprecated //use getUserById and delete this.
    public Optional<User> findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
        }
        return user;
    }

    @Deprecated //move to UserAccessService
    public static boolean isCurrentUserHasRole(UserRoles role) {
        return true; //SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                //.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role.name()));
    }
}

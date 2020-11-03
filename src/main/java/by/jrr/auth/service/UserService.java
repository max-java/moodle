package by.jrr.auth.service;

import by.jrr.api.model.UserContactsDto;
import by.jrr.auth.bean.Role;
import by.jrr.auth.bean.User;
import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.exceptios.UserNameConversionException;
import by.jrr.auth.exceptios.UserServiceException;
import by.jrr.auth.repository.RoleRepository;
import by.jrr.auth.repository.UserRepository;
import by.jrr.email.service.EMailService;
import by.jrr.crm.controller.admin.bean.UserDTO;
//import by.jrr.telegram.bot.service.MessageService;

import by.jrr.message.service.MessageService;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private EMailService eMailService;
    //    @Autowired
//    private MessageService tgMessageService;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    MessageService messageService;
    @Autowired
    ProfileService profileService;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       EMailService eMailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.eMailService = eMailService;
    }

    public User findUserByEmail(String email) {
//        return erasePasswordDataBeforeResponse(userRepository.findByEmail(email)); todo: delete user password data if necessary
        // TODO: 10/06/20 handle org.springframework.dao.IncorrectResultSizeDataAccessException:
        //  query did not return a unique result: 2;
        //  nested exception is javax.persistence.NonUniqueResultException: query did not return a unique result: 2
        return userRepository.findByEmail(email);
    }

    public User findUserByUserName(String userName) {
//        return erasePasswordDataBeforeResponse(userRepository.findByUserName(userName)); todo: delete user password data if necessary
        return userRepository.findByUserName(userName);
    }

    /**
     * original place where new user has been created
     */
    public User saveUser(User user, Optional<UserRoles> userRoleOp) {
        UserRoles newUserRole = userRoleOp.orElseGet(() -> UserRoles.ROLE_GUEST);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);

        Role userRole = roleRepository.findByRole(newUserRole);
        if (userRole == null) { //fix to store new UserRoles ids based on new enum values
            userRole = roleRepository.save(new Role(null, newUserRole));
        }
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }

    private User updateUserPassword(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void registerNewUserAsAdmin(UserDTO userDTO) throws UserServiceException {
        if (ifWordExistAsLoginOrEmail(userDTO.getEmail())) {
            throw new UserServiceException(userDTO.getEmail() + " already exist in database as login or email"); // TODO: 23/06/20 validate users with exceptions
        }
        String password = getRandomPassword();
        User user = User.builder()
                .email(userDTO.getEmail())
                .userName(userDTO.getEmail())
                .name(userDTO.getName())
                .lastName(userDTO.getLastName())
                .firstAndLastName(userDTO.getName() + " " + userDTO.getLastName())
                .phone(userDTO.getPhone())
                .password(password)
                .active(true)
                .build();
        this.saveUser(user, Optional.empty());
        new Thread(() -> eMailService.sendAdminRegisterYouEmailConfirmation(user.getName(), user.getEmail(), password)).start();
    }


    public User quickRegisterUser(String firstAndLastName, String phone, String email) throws UserServiceException {
        if (ifWordExistAsLoginOrEmail(email)) {
            throw new UserServiceException(email + " already exist in database as login or email"); // TODO: 23/06/20 validate users with exceptions
        }
        String password = getRandomPassword();

        String login = email;
        User user = User.builder().email(email).userName(login).phone(phone).password(password).active(true).build();
        user = this.setFirstNameAndLastNameByFirstLastName(firstAndLastName, user);
        final User saveduser = this.saveUser(user, Optional.empty()); // TODO: 10/06/20 consider if user should have different role on registerAndEnroll
        autoLogin(login, password);
        Profile userProfile = profileService.createAndSaveProfileForUser(user);
        System.out.println(" before executing in threads ");

        UserContactsDto userContactsDto = new UserContactsDto(); // TODO: 02/11/2020 replace with mapStruct
        userContactsDto.setEmail(email);
        userContactsDto.setFirstName(saveduser.getName());
        userContactsDto.setLastName(saveduser.getLastName());
        userContactsDto.setPhoneNumber(phone);

        new Thread(() -> eMailService.sendQuickRegostrationConfirmation(email, password, firstAndLastName)).start();
        new Thread(() -> eMailService.amoCrmTrigger(email, firstAndLastName, phone)).start(); // TODO: 17/06/20 move this to stream profile
        new Thread(() -> messageService.sendMessageDtoWitContactData(userContactsDto, userProfile.getId())).start();


        new Thread(() -> messageService.sendMessageDtoWitContactData(userContactsDto, userProfile.getId())).start();

        return saveduser;
    }

    private void autoLogin(String username, String password) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(UserRoles.ROLE_GUEST.name()); // TODO: 05/08/20 this is autologin after registration. consider if I should get it from user object
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(authority));
        authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authToken);
        // TODO: 05/08/20 consider if it right way to autologin in Springboot

//        Collection<SimpleGrantedAuthority> oldAuthorities = (Collection<SimpleGrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();
//        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ANOTHER");
//        List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
//        updatedAuthorities.add(authority);
//        updatedAuthorities.addAll(oldAuthorities);
//
//        SecurityContextHolder.getContext().setAuthentication(
//                new UsernamePasswordAuthenticationToken(
//                        SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
//                        SecurityContextHolder.getContext().getAuthentication().getCredentials(),
//                        updatedAuthorities)
//        );

    }

    private String getRandomPassword() {
        return new Random().ints(6, 33, 122)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public String generateNewPasswordForUser(Long id) {
        Optional<User> userOp = userRepository.findById(id);
        if (userOp.isPresent()) {
            User user = userOp.get();
            String newPass = getRandomPassword();
            user.setPassword(newPass);
            this.updateUserPassword(user);
            // TODO: 30/06/20 send email with hew password
            return newPass;
        } else {
            return "error on updating user Password";
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
// TODO: 06/06/20 replace with template method                                              //
//                                                                                          //
    public void addRoleToUser(UserRoles userRole, Long userId) {                            //
        Role role = roleRepository.findByRole(userRole);
        if (role == null) {
            role = roleRepository.save(new Role(null, userRole));
        }//
        Optional<User> userOp = userRepository.findById(userId);                            //
        if (userOp.isPresent()) {                                                           //
            User user = userOp.get();                                                       //
            user.getRoles().add(role);                                                      //
            userRepository.save(user);                                                      //
        }                                                                                   //
    }                                                                                       //

    public void removeRoleFromUser(UserRoles userRole, Long userId) {                       //
        Role role = roleRepository.findByRole(userRole);                                    //
        Optional<User> userOp = userRepository.findById(userId);                            //
        if (userOp.isPresent()) {                                                           //
            User user = userOp.get();                                                       //
            user.getRoles().remove(role);                                                   //
            userRepository.save(user);                                                      //
        }                                                                                   //
    }                                                                                       //
//                                                                                          //
//                                                                                          //
//////////////////////////////////////////////////////////////////////////////////////////////


    public List<User> findAllUsers() {
        return userRepository.findAll().stream()
//                .map(user -> erasePasswordDataBeforeResponse(user)) //todo: delete user password data if necessary
                .collect(Collectors.toList());
    }

    public Optional<User> findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
//            user = Optional.of(erasePasswordDataBeforeResponse(user.get())); todo: delete user password data if necessary
        }
        return user;
    }

    public static User erasePasswordDataBeforeResponse(User user) {
        user.setPassword("");
        return user;
    }

    private boolean ifWordExistAsLoginOrEmail(String word) {
        if (findUserByUserName(word) != null
                || findUserByEmail(word) != null) {
            return true;
        }
        return false;
    }

    private User setFirstNameAndLastNameByFirstLastName(String firstAndLastName, User user) throws UserNameConversionException {
        String[] name = firstAndLastName.split(" ");
        if (name.length != 2) {
            throw new UserNameConversionException("Expect two words in FirstAndLastName field, but got " + name.length);
        }
        user.setFirstAndLastName(firstAndLastName);
        user.setName(name[0]);
        user.setLastName(name[1]);
        return user;
    }

    public static boolean isCurrentUserHasRole(UserRoles role) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role.name()));
    }
}

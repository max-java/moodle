package by.jrr.auth.service;

import by.jrr.auth.bean.Role;
import by.jrr.auth.bean.User;
import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.exceptios.UserNameConversionException;
import by.jrr.auth.exceptios.UserServiceException;
import by.jrr.auth.repository.RoleRepository;
import by.jrr.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sun.security.util.Password;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
        UserRoles newUserRole = userRoleOp.orElseGet(() -> UserRoles.GUEST);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);

        Role userRole = roleRepository.findByRole(newUserRole);
        if (userRole == null) { //fix to store new UserRoles ids based on new enum values
            userRole = roleRepository.save(new Role(null, newUserRole));
        }
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }

    public User quickRegisterUser(String firstAndLastName, String phone, String email) throws UserServiceException {
        if (ifWordExistAsLoginOrEmail(email)) {
            throw new UserServiceException(email + " already exist in database as login or email");
        }
        String password = new Random().ints(6, 33, 122)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        String login = email;
        User user = User.builder()
                .email(email)
                .userName(login)
                .phone(phone)
                .password(password)
                .active(true)
                .build();
        user = this.setFirstNameAndLastNameByFirstLastName(firstAndLastName, user);
        user = this.saveUser(user, Optional.empty()); // TODO: 10/06/20 consider if user should have different role on registerAndEnroll
        return user;

    }

    //////////////////////////////////////////////////////////////////////////////////////////////
// TODO: 06/06/20 replace with template method                                              //
//                                                                                          //
    public void addRoleToUser(UserRoles userRole, Long userId) {                            //
        Role role = roleRepository.findByRole(userRole);
        if (role == null) {
            role = roleRepository.save(new Role(null, userRole));
        }//
        Optional<User> userOp = userRepository.findById(userId);                            //
        if (userOp.isPresent()) {                                                            //
            User user = userOp.get();                                                       //
            user.getRoles().add(role);                                                      //
            userRepository.save(user);                                                      //
        }                                                                                   //
    }                                                                                       //

    public void removeRoleFromUser(UserRoles userRole, Long userId) {                       //
        Role role = roleRepository.findByRole(userRole);                                    //
        Optional<User> userOp = userRepository.findById(userId);                            //
        if (userOp.isPresent()) {                                                            //
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
}

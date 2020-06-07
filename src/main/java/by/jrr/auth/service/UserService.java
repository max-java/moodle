package by.jrr.auth.service;

import by.jrr.auth.bean.Role;
import by.jrr.auth.bean.User;
import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.repository.RoleRepository;
import by.jrr.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
        return userRepository.findByEmail(email);
    }

    public User findUserByUserName(String userName) {
//        return erasePasswordDataBeforeResponse(userRepository.findByUserName(userName)); todo: delete user password data if necessary
        return userRepository.findByUserName(userName);
    }

    /** original place where new user has been created */
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

//////////////////////////////////////////////////////////////////////////////////////////////
// TODO: 06/06/20 replace with template method                                              //
//                                                                                          //
    public void addRoleToUser(UserRoles userRole, Long userId) {                            //
        Role role = roleRepository.findByRole(userRole);
        if (role == null) {
            role = roleRepository.save(new Role(null, userRole));
        }//
        Optional<User> userOp = userRepository.findById(userId);                            //
        if(userOp.isPresent()) {                                                            //
            User user = userOp.get();                                                       //
            user.getRoles().add(role);                                                      //
            userRepository.save(user);                                                      //
        }                                                                                   //
    }                                                                                       //
    public void removeRoleFromUser(UserRoles userRole, Long userId) {                       //
        Role role = roleRepository.findByRole(userRole);                                    //
        Optional<User> userOp = userRepository.findById(userId);                            //
        if(userOp.isPresent()) {                                                            //
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
        if(user.isPresent()) {
//            user = Optional.of(erasePasswordDataBeforeResponse(user.get())); todo: delete user password data if necessary
        }
        return user;
    }

    public static User erasePasswordDataBeforeResponse(User user) {
        user.setPassword("");
        return user;
    }
}

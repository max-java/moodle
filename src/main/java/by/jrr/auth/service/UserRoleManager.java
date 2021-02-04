package by.jrr.auth.service;

import by.jrr.auth.bean.User;
import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleManager {

    @Autowired
    UserService userService;
    @Autowired
    UserAccessService userAccessService;
    @Autowired
    RoleService roleService;

    public void addRoleIfAbsent(long userId, UserRoles role) {
        if(!userAccessService.isUserHasRole(userId, role)) {
            User user = userService.getUserById(userId);
            user.getRoles().add(roleService.getOrCreate(role));
            userService.updateUser(user);
        }
    }


}

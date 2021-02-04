package by.jrr.auth.service;

import by.jrr.auth.bean.Role;
import by.jrr.auth.bean.UserRoles;
import by.jrr.auth.repository.RoleRepository;
import by.jrr.auth.repository.RoleRepositoryN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    RoleRepositoryN roleRepository;

    public Role getOrCreate(UserRoles userRole) {
        return roleRepository.findByRole(userRole)
                .orElseGet(() -> roleRepository.save(new Role(null, userRole)));
    }
}

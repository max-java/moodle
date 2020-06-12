package by.jrr.auth.repository;

import by.jrr.auth.bean.Role;
import by.jrr.auth.bean.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(UserRoles role);

}

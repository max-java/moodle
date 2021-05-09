package by.jrr.auth.repository;

import by.jrr.auth.bean.KeycloakUser;
import by.jrr.auth.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeycloakUserRepository extends JpaRepository<KeycloakUser, Long> {
    KeycloakUser findByEmail(String email);
    KeycloakUser findByUserName(String userName);
    Optional<KeycloakUser> findByUuid(String uuid);

    Streamable<KeycloakUser> findByUserNameContaining(String userName);
    Streamable<KeycloakUser> findByEmailContaining(String email);
    Streamable<KeycloakUser> findByNameContaining(String name);
    Streamable<KeycloakUser> findByLastNameContaining(String lastName);
}

package by.jrr.auth.repository;

import by.jrr.auth.bean.User;
import org.springframework.data.util.Streamable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByUserName(String userName);

    Streamable<User> findByUserNameContaining(String userName);
    Streamable<User> findByEmailContaining(String email);
    Streamable<User> findByNameContaining(String name);
    Streamable<User> findByLastNameContaining(String lastName);
}

package by.jrr.auth.service;

import by.jrr.auth.bean.User;
import by.jrr.auth.repository.RoleRepository;
import by.jrr.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserSearchService {
    @Autowired private UserRepository userRepository;

    public List<User> searchUserByAllUserFields(String searchTerm) {
        return userRepository.findByUserNameContaining(searchTerm)
                .and(userRepository.findByEmailContaining(searchTerm)
                    .and(userRepository.findByNameContaining(searchTerm)
                        .and(userRepository.findByLastNameContaining(searchTerm))
                    )
                )
                .stream()
                .distinct()// TODO: 26/05/20 не дистинктит. переписать equals & hashcode?
                .map(UserService::erasePasswordDataBeforeResponse)
                .collect(Collectors.toList());
    } // M.Shelkovich: see https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.details

}

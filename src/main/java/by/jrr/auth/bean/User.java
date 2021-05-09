package by.jrr.auth.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id")
    protected long id;

    @Column(name = "user_name") //it is a login )
    @Length(min = 5, message = "Имя пользователя не может быть меньше 5 символов")
    @NotEmpty(message = "Пожалуйста, заполните имя пользователя")
    protected String userName; // TODO: 09/06/20 refactor to "login"

    @Column(name = "email")
    @Email(message = "Реальный емейл важно указать для получения важных сообщений")
    @NotEmpty(message = "Пожалуйста, укажите емейл")
    protected String email;

    @Column(name = "password")
    @Length(min = 5, message = "Пожалуйста, выберите пароль длиннее 5 символов")
    @NotEmpty(message = "Пожалуйста, укажите  пароль")
    protected String password;

    @Column(name = "name")
    @NotEmpty(message = "Пожалуйста, укажите  Имя")
    protected String name;

    @Column(name = "last_name")
    @NotEmpty(message = "Пожалуйста, укажите  Фамилию")
    protected String lastName;

    @Column(name = "first_and_last_name")
    protected String firstAndLastName; //I use it in a registerAndSubscribe form

    @Column(name = "phone")
    @NotEmpty(message = "Пожалуйста, укажите номер телефона, по которому с Вами может связаться куратор")
    protected String phone;

    @Column(name = "active")
    protected Boolean active;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    protected Set<Role> roles;

    public User(User user) {
        this.id = user.id;
        this.userName = user.userName;
        this.email = user.email;
        this.password = user.password;
        this.name = user.name;
        this.lastName = user.lastName;
        this.firstAndLastName = user.firstAndLastName;
        this.phone = user.phone;
        this.active = user.active;
        this.roles = user.roles;
    }

    public String getFullUserName() {
        return name + " " + lastName;
    }
    public String getAllRoles() {
        return roles.stream()
                .map(role -> role.getRole().name())
                .collect(Collectors.joining(", ", "{", "}"));
    }

    public boolean hasRole(UserRoles role) { // TODO: 07/06/20 consider to move in userService and profileService
        List<UserRoles> result = roles.stream()
                .map(r -> r.getRole())
                .filter(r -> r.equals(role))
                .collect(Collectors.toList());
        if (result.size()>0) {
            return true;
        }
        return false;
    }
}

package by.jrr.auth.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Integer id;
    @Column(name = "user_name")
    @Length(min = 5, message = "Имя пользователя не может быть меньше 5 символов")
    @NotEmpty(message = "Пожалуйста, заполните имя пользователя")
    private String userName;
    @Column(name = "email")
    @Email(message = "Реальный емейл важно указать для получения важных сообщений")
    @NotEmpty(message = "Пожалуйста, укажите емейл")
    private String email;
    @Column(name = "password")
    @Length(min = 5, message = "Пожалуйста, выберите пароль длиннее 5 символов")
    @NotEmpty(message = "Укажите пароль")
    private String password;
    @Column(name = "name")
    @NotEmpty(message = "Укажите Имя")
    private String name;
    @Column(name = "last_name")
    @NotEmpty(message = "Укажите Фамилию")
    private String lastName;
    @Column(name = "active")
    private Boolean active;
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

}

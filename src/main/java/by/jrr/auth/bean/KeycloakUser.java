package by.jrr.auth.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class KeycloakUser extends User{
    String uuid;
    String preferredUsername;
    String ssoEmail;

    public KeycloakUser(User user, String uuid, String preferredUsername, String ssoEmail) {
        super(user);
        this.uuid = uuid;
        this.preferredUsername = preferredUsername;
        this.ssoEmail = ssoEmail;
    }
}

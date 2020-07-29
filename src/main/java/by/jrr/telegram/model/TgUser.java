package by.jrr.telegram.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.User;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class TgUser extends User {
    @Id
    private Integer id;
    private Long profileId;

    private String firstName;
    private Boolean isBot;
    private String lastName;
    private String userName;
    private String languageCode;

    public TgUser(User user) {
            this.id = user.getId();
            this.firstName = user.getFirstName();
            this.isBot = user.getBot();
            this.lastName = user.getLastName();
            this.userName = user.getUserName();
            this.languageCode = user.getLanguageCode();

    }

}

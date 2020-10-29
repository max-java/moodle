package by.jrr.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserContacts {
    String email;
    String firstName;
    String lastName;
    String phoneNumber;

    public String asMessageText() {
        return String.format("%s\n%s\n%s\n%s", firstName, lastName, email, phoneNumber);
    }
}

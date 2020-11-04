package by.jrr.telegram.bot.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Contact;

@Data
@Builder
public class TgContact extends Contact {

    private static final String PHONENUMBER_FIELD = "phone_number";
    private static final String FIRSTNAME_FIELD = "first_name";
    private static final String LASTNAME_FIELD = "last_name";
    private static final String USERID_FIELD = "user_id";
    private static final String VCARD_FIELD = "vcard";
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("user_id")
    private Integer userID;
    @JsonProperty("vcard")
    private String vCard;

    public String toString() {
        return "Contact{phoneNumber='" + this.phoneNumber + '\'' + ", firstName='" + this.firstName + '\'' + ", lastName='" + this.lastName + '\'' + ", userID=" + this.userID + ", vCard=" + this.vCard + '}';
    }
}

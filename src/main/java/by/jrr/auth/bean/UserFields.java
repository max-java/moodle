package by.jrr.auth.bean;

import lombok.Getter;

@Getter
public enum UserFields {
    USER_NAME("userName"),
    EMAIL("email"),
    PASSWORD("password"),
    NAME("name"),
    LAST_NAME("lastName"),
    FIRST_AND_LAST_NAME("firstAndLastName"),
    PHONE("phone");

    private String asString;

    UserFields(String asString) {
        this.asString = asString;
    }

}

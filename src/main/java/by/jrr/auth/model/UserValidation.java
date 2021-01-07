package by.jrr.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

public class UserValidation {

    @Data
    public static class Request {
        private final String firstAndLastName;
        private final String email;
        private final String phone;
    }

    @Data
    @NoArgsConstructor
    public static class Response {
        private Status userValidationStatus;
        private Map<Fields, String> errors;

        public void addError(Fields field, String message) {
            if(errors == null) {
                errors = new HashMap<>();
            }
            errors.put(field, message);
        }
    }

    public enum Status {
        OK,
        WARNING,
        ERROR
    }

    @Getter
    @AllArgsConstructor
    public enum Fields {
        FIRST_AND_LAST_NAME("firstAndLastName"),
        EMAIL("email"),
        PHONE("phone");

        String fieldName;
    }
}

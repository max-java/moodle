package by.jrr.auth.model;

import lombok.Data;
import lombok.NoArgsConstructor;

public class DropUserPassword {

    @Data
    @NoArgsConstructor
    public static class Request {
        private Long userId;
    }

    @Data
    @NoArgsConstructor
    public static class Response {
        private String password;
        private String error;
    }
}

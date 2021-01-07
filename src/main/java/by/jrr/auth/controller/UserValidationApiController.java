package by.jrr.auth.controller;

import by.jrr.auth.model.UserValidation;
import by.jrr.auth.service.UserValidationService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.ColumnResult;
import javax.ws.rs.core.MediaType;

@RestController
@CrossOrigin //todo only for testing purpose
public class UserValidationApiController {

    private final UserValidationService userValidationService;

    public UserValidationApiController(UserValidationService userValidationService) {
        this.userValidationService = userValidationService;
    }

    @PostMapping(value = "/auth/validate/user", produces = MediaType.APPLICATION_JSON)
    public UserValidation.Response validateUserData(@RequestBody UserValidation.Request request) {
        return userValidationService.validateUserData(request);
    }
}

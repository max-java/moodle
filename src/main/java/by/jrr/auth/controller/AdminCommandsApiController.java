package by.jrr.auth.controller;

import by.jrr.auth.configuration.annotations.AdminOnly;
import by.jrr.auth.model.DropUserPassword;
import by.jrr.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.MediaType;

@RestController
public class AdminCommandsApiController {

    @Autowired
    UserService userService;

    @AdminOnly
    @PostMapping(value = "/auth/dropUserPassword", produces = MediaType.APPLICATION_JSON)
    public DropUserPassword.Response dropUserPassword(@RequestBody DropUserPassword.Request request) {
        return userService.dropUserPassword(request.getUserId());
    }
}

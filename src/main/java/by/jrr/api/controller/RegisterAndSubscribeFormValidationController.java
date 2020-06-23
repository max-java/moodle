package by.jrr.api.controller;

import by.jrr.auth.service.UserValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RegisterAndSubscribeFormValidationController {

    @Autowired
    UserValidationService userValidationService;

    @GetMapping(value = "/api/registerForm/validate/email/", produces = MediaType.APPLICATION_XML_VALUE)
    public RestResponse validateEmail(@RequestParam String email) {
        RestResponse restResponse = new RestResponse(true);
        try {
            userValidationService.validateEmail(email);
        } catch (Exception ex) {
            restResponse.setValidationPassed(false);
            restResponse.setError(ex.getMessage());
        }
        return restResponse;

    }

    @GetMapping(value = "/api/registerForm/validate/firstAndLastName/", produces = MediaType.APPLICATION_XML_VALUE)
    public RestResponse validateFirstAndLastName(@RequestParam String firstAndLastName) {
        RestResponse restResponse = new RestResponse(true);
        try {
            userValidationService.validateUserFirstAndLastName(firstAndLastName);
        } catch (Exception ex) {
            restResponse.setValidationPassed(false);
            restResponse.setError(ex.getMessage());
        }
        return restResponse;

    }

    @GetMapping(value = "/api/registerForm/validate/phone/", produces = MediaType.APPLICATION_XML_VALUE)
    public RestResponse validatePhone(@RequestParam String phone) {
        RestResponse restResponse = new RestResponse(true);
        if (phone.length() < 7) {
            restResponse.setError("Количество символов в телефонном номере не может быть меньше семи");
            restResponse.setValidationPassed(false);
        }

        return restResponse;
    }
}

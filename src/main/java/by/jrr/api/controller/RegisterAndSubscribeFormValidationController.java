//package by.jrr.api.controller;
//
//import by.jrr.auth.service.UserValidationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.IncorrectResultSizeDataAccessException;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.persistence.NonUniqueResultException;
//
//
//@RestController
//@Deprecated
//public class RegisterAndSubscribeFormValidationController {
//
//    @Autowired
//    UserValidationService userValidationService;
//
//    @GetMapping(value = "/api/registerForm/validate/email/", produces = MediaType.APPLICATION_XML_VALUE)
//    public RestResponse validateEmail(@RequestParam String email) {
//        RestResponse restResponse = new RestResponse(true);
//        try {
//            userValidationService.validateEmail(email);
//        } catch (NonUniqueResultException | IncorrectResultSizeDataAccessException e) {
//            restResponse.setValidationPassed(false);
//            restResponse.setError("Пользователь с e-mail "+ email+ " уже зарегистрирован. Попробуйте другой.");
//            // TODO: 23/06/20 impossible situation, login-email duplicates in DB, add logging
//            // TODO: 23/06/20 It is possible for user, that creates stream | team. Consider to handle that situations
//        } catch (Exception ex) {
//            restResponse.setValidationPassed(false);
//            restResponse.setError(ex.getMessage());
//        }
//        return restResponse;
//    }
//
//    @GetMapping(value = "/api/registerForm/validate/firstAndLastName/", produces = MediaType.APPLICATION_XML_VALUE)
//    public RestResponse validateFirstAndLastName(@RequestParam String firstAndLastName) {
//        RestResponse restResponse = new RestResponse(true);
//        try {
//            userValidationService.validateUserFirstAndLastName(firstAndLastName);
//        } catch (Exception ex) {
//            restResponse.setValidationPassed(false);
//            restResponse.setError(ex.getMessage());
//        }
//        return restResponse;
//
//    }
//
//    @GetMapping(value = "/api/registerForm/validate/phone/", produces = MediaType.APPLICATION_XML_VALUE)
//    public RestResponse validatePhone(@RequestParam String phone) {
//        RestResponse restResponse = new RestResponse(true);
//        if (phone.length() < 7) {
//            restResponse.setError("Количество символов в телефонном номере не может быть меньше семи");
//            restResponse.setValidationPassed(false);
//        }
//
//        return restResponse;
//    }
//}

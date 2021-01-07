package by.jrr.auth.service;

import by.jrr.auth.exceptios.LoginOrEMailExistOnRegistrationException;
import by.jrr.auth.exceptios.UserNameConversionException;
import by.jrr.auth.model.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

import static by.jrr.auth.model.UserValidation.Fields.*;
import static by.jrr.auth.model.UserValidation.Status.*;

@Service
public class UserValidationService {
    @Autowired
    UserService userService;
    public static final short PHONE_LENGTH_MIN = 8;
    public static final short PHONE_LENGTH_MAX = 16;

    //https://en.wikipedia.org/wiki/List_of_country_calling_codes
    public static final String EMAIL_EXIST_ERROR = "email %s уже зарегистрирован";
    public static final String EMAIL_TOO_SHORT_ERROR = "email %s слишком короткий";
    public static final String USER_NAME_TOO_SHORT_ERROR = "Введите два слова: первое - для имени, второе - для фамилии. Введено %s";
    public static final String USER_NAME_TOO_LONG_ERROR = "Достаточно двух слов: первое - для имени, второе - для фамилии. Введено %s";
    public static final String PHONE_NUMBER_FORMAT_ERROR = "Введите мобильный номер телефона, начиная с символа '+'";
    public static final String PHONE_NUMBER_TOO_SHORT_ERROR = "Номер телефона не может быть меньше %s символов, введено %s";
    public static final String PHONE_NUMBER_TOO_LONG_ERROR = "Номер телефона не может быть больше %s символов, введено %s";
    public static final String PHONE_DUMMY_ERROR = "Пожалуйста, введите реальный номер телефона, что бы с вами связался куратор";

    @Deprecated
    public void validateEmail(String email) throws LoginOrEMailExistOnRegistrationException {
        if (checkIfWordExistAsLoginOrEmail(email)) {
            throw new LoginOrEMailExistOnRegistrationException("email " + email + " уже зарегистрирован");
        }
        if (email.length() < 6) {
            throw new LoginOrEMailExistOnRegistrationException("email " + email + " слишком короткий"); // TODO: 23/06/20 создать правильный Exception для этого случая
        }
    }

    @Deprecated
    public void validateUserFirstAndLastName(String firstAndLastName) throws UserNameConversionException {
        String[] name = firstAndLastName.split(" ");
        if (name.length != 2) {
            throw new UserNameConversionException("Ожидалось, что будет введено два слова для имени и фамилии, но фактически заполнено " + name.length);
        }
    }

    private boolean checkIfWordExistAsLoginOrEmail(String word) {
        if (userService.findUserByUserName(word) != null
                || userService.findUserByEmail(word) != null) {
            return true;
        }
        return false;
    }

    public UserValidation.Response validateUserData(UserValidation.Request request) {
        UserValidation.Response response = makeInitialUserValidationResponse();
        validateFirstAndLastName(request.getFirstAndLastName(), response);
        validateEmail(request.getEmail(), response);
        validatePhone(request.getPhone(), response);
        updateStatus(response);
        return response;
    }

    private void validateFirstAndLastName(String firstAndLastName, UserValidation.Response response) {
        if (valueIsPresent(firstAndLastName)) {
            String[] name = firstAndLastName.trim().split(" ");
            if (name.length < 2) {
                response.addError(FIRST_AND_LAST_NAME, String.format(USER_NAME_TOO_SHORT_ERROR, name.length));
            } else if (name.length > 2) {
                response.addError(FIRST_AND_LAST_NAME, String.format(USER_NAME_TOO_LONG_ERROR, name.length));
            }
        } else {
            response.setUserValidationStatus(ERROR);
        }
    }

    private void validateEmail(String email, UserValidation.Response response) {
        if (valueIsPresent(email)) {
            if (email.length() < 6) {
                response.addError(EMAIL, String.format(EMAIL_TOO_SHORT_ERROR, email));
            } else if (checkIfWordExistAsLoginOrEmail(email)) {
                response.addError(EMAIL, String.format(EMAIL_EXIST_ERROR, email));
            }
        } else {
            response.setUserValidationStatus(ERROR);
        }
    }

    private void validatePhone(String phone, UserValidation.Response response) {
        if (valueIsPresent(phone)) {
            if (phone.charAt(0) != '+') {
                response.addError(PHONE, PHONE_NUMBER_FORMAT_ERROR);
            } else if (phone.length() < PHONE_LENGTH_MIN) {
                response.addError(PHONE, String.format(PHONE_NUMBER_TOO_SHORT_ERROR, PHONE_LENGTH_MIN, phone.length()));
            } else if (phone.length() > PHONE_LENGTH_MAX) {
                response.addError(PHONE, String.format(PHONE_NUMBER_TOO_LONG_ERROR, PHONE_LENGTH_MAX, phone.length()));
            } else if (isDummyPhoneNumber(phone)) {
                response.addError(PHONE, PHONE_DUMMY_ERROR);
            }
        } else {
            response.setUserValidationStatus(ERROR);
        }
    }

    private void updateStatus(UserValidation.Response response) {
        if (response.getUserValidationStatus().equals(OK)) {
            if (response.getErrors() != null && response.getErrors().size() > 0) {
                response.setUserValidationStatus(ERROR);
            }
        }
    }

    private UserValidation.Response makeInitialUserValidationResponse() {
        UserValidation.Response response = new UserValidation.Response();
        response.setUserValidationStatus(OK);
        return response;
    }

    private boolean valueIsPresent(String value) {
        if (value != null && !value.isEmpty()) {
            return true;
        }
        return false;
    }

    private boolean isDummyPhoneNumber(@NotNull String phoneNumber) {
        return false; //todo
    }
}

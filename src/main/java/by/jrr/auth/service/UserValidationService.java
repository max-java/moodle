package by.jrr.auth.service;

import by.jrr.auth.exceptios.LoginOrEMailExistOnRegistrationException;
import by.jrr.auth.exceptios.UserNameConversionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserValidationService {
    @Autowired
    UserService userService;

    public void validateEmail(String email) throws LoginOrEMailExistOnRegistrationException {
        if(checkIfWordExistAsLoginOrEmail(email)) {
            throw new LoginOrEMailExistOnRegistrationException("email "+ email +" уже зарегистрирован");
        }
        if(email.length() < 6) {
            throw new LoginOrEMailExistOnRegistrationException("email "+ email +" слишком короткий"); // TODO: 23/06/20 создать правильный Exception для этого случая
        }
    }

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


    // TODO: 23/06/20 consider to add phone number validation

}

package by.jrr.auth.exceptios;

public class LoginOrEMailExistOnRegistrationException extends UserServiceException {
    public LoginOrEMailExistOnRegistrationException(String message) {
        super(message);
    }
}

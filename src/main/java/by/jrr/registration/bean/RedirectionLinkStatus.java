package by.jrr.registration.bean;

public enum RedirectionLinkStatus {
    NEW ("If you are not redirected automatically, follow this link:"),    //link has not been used
    USED ("Redirection link could be used just once. This one already used. Please, generate another one."),   //link used
    EXPIRED ("Redirection link has been expired. Please, generate another one."); //link has attempt to be used after been expired

    RedirectionLinkStatus(String message) {
        this.message = message;
    }

    private String message;

    public String getMessage() {
        return this.message;
    }
}

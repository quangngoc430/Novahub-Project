package vn.novahub.helpdesk.exception;

public class TokenNotFoundException extends Exception {

    public TokenNotFoundException(String accessToken){
        super("TokenNotFoundException with accessToken = " + accessToken);
    }
}

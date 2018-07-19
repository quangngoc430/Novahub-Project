package vn.novahub.helpdesk.exception;

public class TokenIsExpiredException extends Exception {

    public TokenIsExpiredException(String token){
        super("TokenNotFoundException with token = " + token);
    }
}

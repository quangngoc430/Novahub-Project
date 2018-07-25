package vn.novahub.helpdesk.exception;

public class TokenIsExpiredException extends Exception {

    public TokenIsExpiredException(String token){
        super("TokenIsExpiredException with token = " + token);
    }
}

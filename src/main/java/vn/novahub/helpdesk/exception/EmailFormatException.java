package vn.novahub.helpdesk.exception;

public class EmailFormatException extends Exception{

    public EmailFormatException(String email){
        super("EmailFormatException with email = " + email);
    }
}

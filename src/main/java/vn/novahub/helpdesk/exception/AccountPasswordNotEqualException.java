package vn.novahub.helpdesk.exception;

public class AccountPasswordNotEqualException extends Exception {

    public AccountPasswordNotEqualException(String message){
        super("AccountPasswordNotEqual with message = " + message);
    }
}

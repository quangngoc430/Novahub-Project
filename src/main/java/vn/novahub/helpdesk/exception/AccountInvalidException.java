package vn.novahub.helpdesk.exception;

public class AccountInvalidException extends Exception{

    public AccountInvalidException(String message){
        super("AccountInvalidException with message = " + message);
    }

    public AccountInvalidException(){
        super("AccountInvalidException");
    }
}

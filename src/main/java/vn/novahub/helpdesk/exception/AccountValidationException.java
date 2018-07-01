package vn.novahub.helpdesk.exception;

import java.util.HashMap;

public class AccountValidationException extends Exception{

    private HashMap<String, String> errors;

    public HashMap<String, String> getErrors() {
        return errors;
    }

    public void setErrors(HashMap<String, String> errors) {
        this.errors = errors;
    }

    public AccountValidationException(long numberOfErrors, HashMap<String, String> errors){
        super("AccountValidationException with number of errors = " + numberOfErrors);
        this.errors = errors;
    }
}

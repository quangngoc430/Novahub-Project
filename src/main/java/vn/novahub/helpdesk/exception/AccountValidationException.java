package vn.novahub.helpdesk.exception;

import java.util.Map;

public class AccountValidationException extends Exception{

    private Map<String, String> errors;

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public AccountValidationException(long numberOfErrors, Map<String, String> errors){
        super("AccountValidationException with number of errors = " + numberOfErrors);
        this.errors = errors;
    }
}

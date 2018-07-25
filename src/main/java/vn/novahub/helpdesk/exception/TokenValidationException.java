package vn.novahub.helpdesk.exception;

import java.util.Map;

public class TokenValidationException extends Exception{

    private Map<String, String> errors;

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public TokenValidationException(long numberOfErrors, Map<String, String> errors){
        super("TokenValidationException with number of errors = " + numberOfErrors);
        this.errors = errors;
    }
}

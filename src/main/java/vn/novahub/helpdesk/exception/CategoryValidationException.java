package vn.novahub.helpdesk.exception;

import java.util.Map;

public class CategoryValidationException extends Exception {

    private Map<String, String> errors;

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public CategoryValidationException(long numberOfErrors, Map<String, String> errors){
        super("CategoryValidationException with number of errors = " + numberOfErrors);
        this.errors = errors;
    }
}

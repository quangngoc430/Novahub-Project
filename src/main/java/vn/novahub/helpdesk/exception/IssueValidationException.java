package vn.novahub.helpdesk.exception;

import java.util.Map;

public class IssueValidationException extends Exception {

    private Map<String, String> errors;

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public IssueValidationException(long numberOfErrors, Map<String, String> errors){
        super("IssueValidationException with number of errors = " + numberOfErrors);
        this.errors = errors;
    }
}

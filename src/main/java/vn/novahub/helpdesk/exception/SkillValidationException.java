package vn.novahub.helpdesk.exception;

import java.util.Map;

public class SkillValidationException extends Exception {

    private Map<String, String> errors;

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public SkillValidationException(long numberOfErrors, Map<String, String> errors){
        super("SkillValidationException with number of errors = " + numberOfErrors);
        this.errors = errors;
    }
}

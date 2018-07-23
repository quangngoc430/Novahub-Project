package vn.novahub.helpdesk.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IssueStatusConstraintValidator implements ConstraintValidator<IssueStatus, String> {

    private String[] statusList;

    @Override
    public void initialize(IssueStatus constraintAnnotation) {
        statusList = constraintAnnotation.statuses();
    }

    @Override
    public boolean isValid(String status, ConstraintValidatorContext constraintValidatorContext) {

        for(String eachStatus : statusList){
            if(status.equals(eachStatus))
                return true;
        }

        return false;
    }
}

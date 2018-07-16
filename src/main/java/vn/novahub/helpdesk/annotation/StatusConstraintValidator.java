package vn.novahub.helpdesk.annotation;

import vn.novahub.helpdesk.enums.AccountStatus;
import vn.novahub.helpdesk.enums.IssueStatus;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Issue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.groups.Default;

public class StatusConstraintValidator implements ConstraintValidator<Status, String> {

    private Class<?> targetClass;

    @Override
    public void initialize(Status constraintAnnotation) {
        targetClass = constraintAnnotation.targetClass();
    }

    @Override
    public boolean isValid(String status, ConstraintValidatorContext constraintValidatorContext) {

        if(targetClass == Issue.class){
            return checkIssueStatus(status);
        }

        if(targetClass == Account.class){
            return checkAccountStatus(status);
        }

        return false;
    }

    private boolean checkIssueStatus(String status){
        if(IssueStatus.APPROVE.name().equals(status))
            return true;
        if(IssueStatus.DENY.name().equals(status))
            return true;
        if(IssueStatus.PENDING.name().equals(status))
            return true;

        return false;
    }

    private boolean checkAccountStatus(String status){
        if(AccountStatus.ACTIVE.name().equals(status))
            return true;
        if(AccountStatus.INACTIVE.name().equals(status))
            return true;
        if(AccountStatus.LOCKED.name().equals(status))
            return true;

        return false;
    }
}

package vn.novahub.helpdesk.annotation;

import vn.novahub.helpdesk.enums.AccountEnum;
import vn.novahub.helpdesk.enums.IssueEnum;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Issue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StatusConstraintValidator implements ConstraintValidator<Status, String> {

    private Class<?> targetClass;

    @Override
    public void initialize(Status constraintAnnotation) {
        targetClass = constraintAnnotation.targetClass();
    }

    @Override
    public boolean isValid(String status, ConstraintValidatorContext constraintValidatorContext) {

        if(targetClass == Issue.class) {
            return checkIssueStatus(status);
        }

        if(targetClass == Account.class) {
            return checkAccountStatus(status);
        }

        return false;
    }

    private boolean checkIssueStatus(String status) {
        if(IssueEnum.APPROVE.name().equals(status))
            return true;
        if(IssueEnum.DENY.name().equals(status))
            return true;
        if(IssueEnum.PENDING.name().equals(status))
            return true;

        return false;
    }

    private boolean checkAccountStatus(String status) {
        if(AccountEnum.ACTIVE.name().equals(status))
            return true;
        if(AccountEnum.INACTIVE.name().equals(status))
            return true;
        if(AccountEnum.LOCKED.name().equals(status))
            return true;

        return false;
    }
}

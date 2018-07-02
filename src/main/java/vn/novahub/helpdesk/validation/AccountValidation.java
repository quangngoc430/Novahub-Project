package vn.novahub.helpdesk.validation;

import vn.novahub.helpdesk.exception.AccountValidationException;
import vn.novahub.helpdesk.model.Account;

public interface AccountValidation {

    void validate(Account account, Class groupClassValidation) throws AccountValidationException;
}

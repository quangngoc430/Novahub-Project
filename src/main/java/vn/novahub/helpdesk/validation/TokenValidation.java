package vn.novahub.helpdesk.validation;

import vn.novahub.helpdesk.exception.AccountValidationException;
import vn.novahub.helpdesk.model.Token;

public interface TokenValidation {
    void validate(Token token, Class groupClassValidation) throws AccountValidationException;
}

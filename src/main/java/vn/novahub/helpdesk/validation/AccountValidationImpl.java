package vn.novahub.helpdesk.validation;

import org.springframework.stereotype.Component;
import vn.novahub.helpdesk.exception.AccountValidationException;
import vn.novahub.helpdesk.model.Account;

import javax.validation.*;
import java.util.HashMap;
import java.util.Set;

@Component
public class AccountValidationImpl implements AccountValidation {

    private static final Validator validator;

    static {
        Configuration<?> config = Validation.byDefaultProvider().configure();
        ValidatorFactory factory = config.buildValidatorFactory();
        validator = factory.getValidator();
        factory.close();
    }

    @Override
    public void validate(Account account, Class groupClassValidation) throws AccountValidationException {
        Set<ConstraintViolation<Account>> constraintViolations = validator.validate(account, groupClassValidation);

        HashMap<String, String> errors = new HashMap<>();

        if(!constraintViolations.isEmpty()){
            for(ConstraintViolation<Account> commentConstraintViolation : constraintViolations){
                errors.put(commentConstraintViolation.getPropertyPath().toString(), commentConstraintViolation.getMessage());
            }

            throw new AccountValidationException(constraintViolations.size(), errors);
        }
    }
}

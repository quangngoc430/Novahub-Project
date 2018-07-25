package vn.novahub.helpdesk.validation;

import org.springframework.stereotype.Component;
import vn.novahub.helpdesk.exception.AccountValidationException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Token;

import javax.validation.*;
import java.util.HashMap;
import java.util.Set;

@Component
public class TokenValidationImpl implements TokenValidation{

    private static final Validator validator;

    static {
        Configuration<?> config = Validation.byDefaultProvider().configure();
        ValidatorFactory factory = config.buildValidatorFactory();
        validator = factory.getValidator();
        factory.close();
    }

    @Override
    public void validate(Token token, Class groupClassValidation) throws AccountValidationException {
        Set<ConstraintViolation<Token>> constraintViolations = validator.validate(token, groupClassValidation);

        HashMap<String, String> errors = new HashMap<>();

        if(!constraintViolations.isEmpty()){
            for(ConstraintViolation<Token> commentConstraintViolation : constraintViolations){
                errors.put(commentConstraintViolation.getPropertyPath().toString(), commentConstraintViolation.getMessage());
            }

            throw new AccountValidationException(constraintViolations.size(), errors);
        }
    }
}

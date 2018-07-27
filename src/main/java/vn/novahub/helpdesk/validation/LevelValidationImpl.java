package vn.novahub.helpdesk.validation;

import org.springframework.stereotype.Component;
import vn.novahub.helpdesk.exception.LevelValidationException;
import vn.novahub.helpdesk.model.Level;

import javax.validation.*;
import java.util.HashMap;
import java.util.Set;

@Component
public class LevelValidationImpl implements LevelValidation{

    private static final Validator validator;

    static {
        Configuration<?> config = Validation.byDefaultProvider().configure();
        ValidatorFactory factory = config.buildValidatorFactory();
        validator = factory.getValidator();
        factory.close();
    }

    @Override
    public void validate(Level level, Class classGroupValidation) throws LevelValidationException {
        Set<ConstraintViolation<Level>> constraintViolations = validator.validate(level, classGroupValidation);

        HashMap<String, String> errors = new HashMap<>();

        if(!constraintViolations.isEmpty()){
            for(ConstraintViolation<Level> commentConstraintViolation : constraintViolations){
                errors.put(commentConstraintViolation.getPropertyPath().toString(), commentConstraintViolation.getMessage());
            }

            throw new LevelValidationException(constraintViolations.size(), errors);
        }
    }
}

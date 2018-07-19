package vn.novahub.helpdesk.validation;

import org.springframework.stereotype.Component;
<<<<<<< HEAD
import vn.novahub.helpdesk.exception.CategoryValidationException;
import vn.novahub.helpdesk.exception.SkillValidationException;
import vn.novahub.helpdesk.model.Category;
=======
import vn.novahub.helpdesk.exception.SkillValidationException;
>>>>>>> develop
import vn.novahub.helpdesk.model.Skill;

import javax.validation.*;
import java.util.HashMap;
import java.util.Set;

@Component
public class SkillValidationImpl implements SkillValidation {

    private static final Validator validator;

    static {
        Configuration<?> config = Validation.byDefaultProvider().configure();
        ValidatorFactory factory = config.buildValidatorFactory();
        validator = factory.getValidator();
        factory.close();
    }

    @Override
    public void validate(Skill skill, Class groupClassValidation) throws SkillValidationException {
        Set<ConstraintViolation<Skill>> constraintViolations = validator.validate(skill, groupClassValidation);

        HashMap<String, String> errors = new HashMap<>();

        if(!constraintViolations.isEmpty()){
            for(ConstraintViolation<Skill> commentConstraintViolation : constraintViolations){
                errors.put(commentConstraintViolation.getPropertyPath().toString(), commentConstraintViolation.getMessage());
            }

            throw new SkillValidationException(constraintViolations.size(), errors);
        }
    }
}

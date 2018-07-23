package vn.novahub.helpdesk.validation;

import org.springframework.stereotype.Component;
import vn.novahub.helpdesk.exception.IssueValidationException;
import vn.novahub.helpdesk.exception.SkillValidationException;
import vn.novahub.helpdesk.model.Issue;

import javax.validation.*;
import java.util.HashMap;
import java.util.Set;

@Component
public class IssueValidationImpl implements IssueValidation {

    private static final Validator validator;

    static {
        Configuration<?> config = Validation.byDefaultProvider().configure();
        ValidatorFactory factory = config.buildValidatorFactory();
        validator = factory.getValidator();
        factory.close();
    }

    @Override
    public void validate(Issue issue, Class classGroupValidation) throws IssueValidationException {
        Set<ConstraintViolation<Issue>> constraintViolations = validator.validate(issue, classGroupValidation);

        HashMap<String, String> errors = new HashMap<>();

        if(!constraintViolations.isEmpty()){
            for(ConstraintViolation<Issue> commentConstraintViolation : constraintViolations){
                errors.put(commentConstraintViolation.getPropertyPath().toString(), commentConstraintViolation.getMessage());
            }

            throw new IssueValidationException(constraintViolations.size(), errors);
        }
    }
}

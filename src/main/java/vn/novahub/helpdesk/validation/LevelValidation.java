package vn.novahub.helpdesk.validation;

import vn.novahub.helpdesk.exception.LevelValidationException;
import vn.novahub.helpdesk.exception.SkillValidationException;
import vn.novahub.helpdesk.model.Level;

public interface LevelValidation {

    void validate(Level level, Class classGroupValidation) throws LevelValidationException;

}

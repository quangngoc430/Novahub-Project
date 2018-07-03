package vn.novahub.helpdesk.validation;

import vn.novahub.helpdesk.exception.SkillValidationException;
import vn.novahub.helpdesk.model.Skill;

public interface SkillValidation {

    void validate(Skill skill, Class classGroupValidation) throws SkillValidationException;
}

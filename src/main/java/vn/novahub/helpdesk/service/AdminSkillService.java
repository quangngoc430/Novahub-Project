package vn.novahub.helpdesk.service;

import vn.novahub.helpdesk.exception.CategoryNotFoundException;
import vn.novahub.helpdesk.exception.SkillIsExistException;
import vn.novahub.helpdesk.exception.SkillNotFoundException;
import vn.novahub.helpdesk.exception.SkillValidationException;
import vn.novahub.helpdesk.model.Skill;

public interface AdminSkillService {

    Skill create(Skill skill) throws SkillValidationException, SkillIsExistException;

    Skill update(long skillId, Skill skill) throws SkillValidationException, SkillIsExistException, SkillNotFoundException, CategoryNotFoundException;

    void delete(long skillId) throws SkillNotFoundException;

    Skill createByCategoryId(Skill skill, long categoryId) throws CategoryNotFoundException, SkillValidationException, SkillIsExistException;

    Skill updateByCategoryIdAndSkillId(Skill skill, long categoryId, long skillId) throws SkillNotFoundException, SkillValidationException, SkillIsExistException;

    void deleteByCategoryIdAndSkillId(long categoryId, long skillId) throws SkillNotFoundException;
}

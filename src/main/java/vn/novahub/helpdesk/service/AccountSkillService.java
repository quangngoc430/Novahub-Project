package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.CategoryNotFoundException;
import vn.novahub.helpdesk.exception.SkillIsExistException;
import vn.novahub.helpdesk.exception.SkillNotFoundException;
import vn.novahub.helpdesk.exception.SkillValidationException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Skill;

public interface AccountSkillService {

    Skill findOne(long skillId) throws SkillNotFoundException;

    Page<Account> getAllUsersBySkillId(long skillId, Pageable pageable) throws SkillNotFoundException;

    Page<Skill> getAllByKeyword(String keyword, Pageable pageable);

    Page<Skill> getAllByKeywordForAccountLogin(String keyword, Pageable pageable);

    Skill create(Skill skill) throws SkillValidationException, SkillIsExistException;

    Skill update(long skillId, Skill skill) throws SkillValidationException, SkillNotFoundException;

    void delete(long skillId) throws SkillNotFoundException;

    Skill getByCategoryIdAndSkillId(long categoryId, long skillId) throws SkillNotFoundException;

    Page<Skill> getAllByCategoryIdAndName(long categoryId, String name, Pageable pageable) throws CategoryNotFoundException;

}

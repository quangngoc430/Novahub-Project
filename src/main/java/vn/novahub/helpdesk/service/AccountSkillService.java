package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Skill;

public interface AccountSkillService {

    Skill findOne(long skillId) throws SkillNotFoundException;

    Page<Account> getAllUsersBySkillId(long skillId, Pageable pageable) throws SkillNotFoundException;

    Page<Skill> getAllByKeyword(long categoryId, String keyword, Pageable pageable) throws CategoryNotFoundException;

    Page<Skill> getAllByKeywordForAccountLogin(long categoryId, String keyword, Pageable pageable) throws CategoryNotFoundException;

    Skill create(Skill skill) throws SkillValidationException, SkillIsExistException, CategoryNotFoundException, LevelValidationException;

    Skill update(long skillId, Skill newSkill) throws SkillNotFoundException, LevelValidationException, SkillValidationException, SkillIsExistException;

    void delete(long skillId) throws SkillNotFoundException;

    Skill getByCategoryIdAndSkillId(long categoryId, long skillId) throws SkillNotFoundException;

    Page<Skill> getAllByCategoryIdAndName(long categoryId, String name, Pageable pageable) throws CategoryNotFoundException;

    Page<Skill> getAllByAccountId(long accountId, Pageable pageable) throws AccountNotFoundException;

}

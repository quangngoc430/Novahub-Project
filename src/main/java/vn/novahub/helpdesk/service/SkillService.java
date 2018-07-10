package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.CategoryNotFoundException;
import vn.novahub.helpdesk.exception.SkillIsExistException;
import vn.novahub.helpdesk.exception.SkillNotFoundException;
import vn.novahub.helpdesk.exception.SkillValidationException;
<<<<<<< HEAD
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Skill;

import javax.servlet.http.HttpServletRequest;

public interface SkillService {

    Skill getById(long skillId) throws SkillNotFoundException;

    Page<Skill> getAllByKeyword(String keyword, Pageable pageable);

    Skill createByAdmin(Skill skill) throws SkillValidationException, SkillIsExistException;

    Skill updateByAdmin(long skillId, Skill skill) throws SkillValidationException, SkillIsExistException, SkillNotFoundException;
=======
import vn.novahub.helpdesk.model.Skill;

public interface SkillService {

    Skill create(Skill skill) throws SkillValidationException;

    Skill createByCategoryId(Skill skill, long categoryId) throws SkillValidationException, SkillIsExistException, CategoryNotFoundException;

    Skill updateBySkillId(Skill skill, long skillId);

    Skill updateByCategoryIdAndSkillId(Skill skill, long categoryId, long skillId) throws CategoryNotFoundException, SkillNotFoundException, SkillValidationException, SkillIsExistException;
>>>>>>> develop

    void deleteByAdmin(long skillId) throws SkillNotFoundException;

    Page<Account> getAllUsersOfASkill(long skillId, Pageable pageable) throws SkillNotFoundException;

    Page<Skill> getAllByKeywordForAccountLogin(String keyword, Pageable pageable);

    Skill createForAccountLogin(Skill skill) throws SkillValidationException, SkillIsExistException;

<<<<<<< HEAD
    Skill updateForAccountLogin(long skillId, Skill skill) throws SkillValidationException, SkillNotFoundException;
=======
    Page<Skill> getAllByName(String name, Pageable pageable);
>>>>>>> develop

    void deleteForAccountLogin(long skillId) throws SkillNotFoundException;

    Skill createByCategoryId(Skill skill, long categoryId);

    Skill updateByCategoryIdAndSkillId(Skill skill, long categoryId, long skillId) throws CategoryNotFoundException, SkillNotFoundException;

    Page<Skill> getAllByCategoryIdAndName(long categoryId, String name, Pageable pageable) throws CategoryNotFoundException;

    Skill getByCategoryIdAndSkillId(long categoryId, long skillId) throws SkillNotFoundException;

<<<<<<< HEAD
    void deleteByCategoryIdAndSkillId(long categoryId, long skillId) throws CategoryNotFoundException, SkillNotFoundException;
=======
    void deleteByCategoryIdAndSkillId(long categoryId, long skillId) throws SkillNotFoundException;
>>>>>>> develop

}

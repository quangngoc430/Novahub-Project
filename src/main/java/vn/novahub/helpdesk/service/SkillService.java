package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.CategoryNotFoundException;
import vn.novahub.helpdesk.exception.SkillIsExistException;
import vn.novahub.helpdesk.exception.SkillNotFoundException;
import vn.novahub.helpdesk.exception.SkillValidationException;
import vn.novahub.helpdesk.model.Skill;

import javax.servlet.http.HttpServletRequest;

public interface SkillService {

    Skill create(Skill skill, HttpServletRequest request) throws SkillValidationException;

    Skill createByCategoryId(Skill skill, long categoryId) throws SkillValidationException, SkillIsExistException;

    Skill updateBySkillId(Skill skill, long skillId, HttpServletRequest request);

    Skill updateByCategoryIdAndSkillId(Skill skill, long categoryId, long skillId) throws CategoryNotFoundException, SkillNotFoundException, SkillValidationException, SkillIsExistException;

    Page<Skill> getAllByCategoryIdAndName(long categoryId, String name, Pageable pageable) throws CategoryNotFoundException;

    Page<Skill> getAllByName(String name, Pageable pageable, HttpServletRequest request);

    Skill getBySkillId(long skillId) throws SkillNotFoundException;

    Skill getByCategoryIdAndSkillId(long categoryId, long skillId) throws SkillNotFoundException;

    void deleteByCategoryIdAndSkillId(long categoryId, long skillId) throws SkillNotFoundException;

}

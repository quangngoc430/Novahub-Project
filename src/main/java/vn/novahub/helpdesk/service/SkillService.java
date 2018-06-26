package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.CategoryNotFoundException;
import vn.novahub.helpdesk.exception.SkillNotFoundException;
import vn.novahub.helpdesk.model.Skill;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public interface SkillService {

    Skill create(Skill skill, HttpServletRequest request);

    Skill createByCategoryId(Skill skill, long categoryId);

    Skill updateBySkillId(Skill skill, long skillId, HttpServletRequest request);

    Skill updateByCategoryIdAndSkillId(Skill skill, long categoryId, long skillId) throws CategoryNotFoundException, SkillNotFoundException;

    Page<Skill> getAllByCategoryIdAndName(long categoryId, String name, Pageable pageable) throws CategoryNotFoundException;

    Page<Skill> getAllByName(String name, Pageable pageable, HttpServletRequest request);

    Skill getBySkillId(long skillId) throws SkillNotFoundException;

    Skill getByCategoryIdAndSkillId(long categoryId, long skillId);

    void deteleByCategoryIdAndSkillId(long categoryId, long skillId) throws CategoryNotFoundException, SkillNotFoundException;

}

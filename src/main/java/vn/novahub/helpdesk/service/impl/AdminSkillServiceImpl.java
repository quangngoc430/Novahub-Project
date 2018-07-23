package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.CategoryNotFoundException;
import vn.novahub.helpdesk.exception.SkillIsExistException;
import vn.novahub.helpdesk.exception.SkillNotFoundException;
import vn.novahub.helpdesk.exception.SkillValidationException;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.repository.CategoryRepository;
import vn.novahub.helpdesk.repository.SkillRepository;
import vn.novahub.helpdesk.service.AdminSkillService;
import vn.novahub.helpdesk.validation.GroupCreateSkill;
import vn.novahub.helpdesk.validation.GroupUpdateSkill;
import vn.novahub.helpdesk.validation.SkillValidation;

import java.util.Date;

@Service
public class AdminSkillServiceImpl implements AdminSkillService {

    @Autowired
    private SkillValidation skillValidation;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Skill create(Skill skill) throws SkillValidationException, SkillIsExistException {

        skillValidation.validate(skill, GroupCreateSkill.class);

        Skill oldSkill = skillRepository.getByNameAndLevelAndCategoryId(skill.getName(), skill.getLevel(), skill.getCategoryId());

        if(oldSkill != null) {
            throw new SkillIsExistException(skill.getName(), skill.getLevel(), skill.getCategoryId());
        }

        skill.setCreatedAt(new Date());
        skill.setUpdatedAt(new Date());
        skill = skillRepository.save(skill);
        skill.setCategory(categoryRepository.getById(skill.getCategoryId()));

        return skill;
    }

    @Override
    public Skill update(long skillId, Skill skill) throws SkillValidationException, SkillIsExistException, SkillNotFoundException, CategoryNotFoundException {
        skillValidation.validate(skill, GroupUpdateSkill.class);

        Skill oldSkill = skillRepository.getById(skillId);

        if (oldSkill == null) {
            throw new SkillNotFoundException(skillId);
        }

        if (oldSkill.equals(skill)){
            return oldSkill;
        }

        if(skillRepository.existsByNameAndLevelAndCategoryId(skill.getName(), skill.getLevel(), skill.getCategoryId()))
            throw new SkillIsExistException(skill.getName(), skill.getLevel(), skill.getCategoryId());

        if(!categoryRepository.existsById(skill.getCategoryId()))
            throw new CategoryNotFoundException(skill.getCategoryId());

        oldSkill.setName(skill.getName());
        oldSkill.setLevel(skill.getLevel());
        oldSkill.setCategoryId(skill.getCategoryId());
        oldSkill.setUpdatedAt(new Date());

        oldSkill = skillRepository.save(oldSkill);
        oldSkill.setCategory(categoryRepository.getById(oldSkill.getCategoryId()));

        return oldSkill;
    }

    @Override
    public void delete(long skillId) throws SkillNotFoundException {

        if(!skillRepository.existsById(skillId))
            throw new SkillNotFoundException(skillId);

        skillRepository.deleteById(skillId);
    }

    @Override
    public Skill createByCategoryId(Skill skill, long categoryId) throws CategoryNotFoundException, SkillValidationException, SkillIsExistException {

        if(!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException(categoryId);

        skill.setCategoryId(categoryId);
        skillValidation.validate(skill, GroupCreateSkill.class);

        if(skillRepository.existsByNameAndLevelAndCategoryId(skill.getName(), skill.getLevel(), skill.getCategoryId()))
            throw new SkillIsExistException(skill.getName(), skill.getLevel(), skill.getCategoryId());

        skill.setCreatedAt(new Date());
        skill.setUpdatedAt(new Date());

        skill = skillRepository.save(skill);
        skill.setCategory(categoryRepository.getById(categoryId));

        return skill;
    }

    @Override
    public Skill updateByCategoryIdAndSkillId(Skill skill, long categoryId, long skillId) throws CategoryNotFoundException, SkillNotFoundException, SkillValidationException, SkillIsExistException {
        if(!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException(categoryId);

        Skill oldSkill = skillRepository.getByIdAndCategoryId(skillId, categoryId);

        if(oldSkill == null)
            throw new SkillNotFoundException(skillId);

        skill.setCategoryId(categoryId);
        skillValidation.validate(skill, GroupUpdateSkill.class);

        if(oldSkill.equals(skill))
            return oldSkill;

        if(skillRepository.existsByNameAndLevelAndCategoryId(skill.getName(), skill.getLevel(), categoryId))
            throw new SkillIsExistException(skill.getName(), skill.getLevel(), categoryId);

        oldSkill.setLevel(skill.getLevel());
        oldSkill.setName(skill.getName());
        oldSkill.setUpdatedAt(new Date());

        return skillRepository.save(oldSkill);
    }

    @Override
    public void deleteByCategoryIdAndSkillId(long categoryId, long skillId) throws SkillNotFoundException {

        if(!skillRepository.existsByIdAndCategoryId(skillId, categoryId))
            throw new SkillNotFoundException(skillId, categoryId);

        skillRepository.deleteByIdAndCategoryId(skillId, categoryId);
    }
}

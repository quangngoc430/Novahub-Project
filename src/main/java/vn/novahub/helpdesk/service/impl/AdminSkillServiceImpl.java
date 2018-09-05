package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.CategoryNotFoundException;
import vn.novahub.helpdesk.exception.SkillIsExistException;
import vn.novahub.helpdesk.exception.SkillNotFoundException;
import vn.novahub.helpdesk.exception.SkillValidationException;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.repository.CategoryRepository;
import vn.novahub.helpdesk.repository.SkillRepository;
import vn.novahub.helpdesk.service.AdminSkillService;
import vn.novahub.helpdesk.validation.GroupCreateSkill;
import vn.novahub.helpdesk.validation.GroupUpdateSkill;
import vn.novahub.helpdesk.validation.SkillValidation;

import javax.validation.groups.Default;
import java.util.Date;
import java.util.Optional;

@Service
public class AdminSkillServiceImpl implements AdminSkillService {

    @Autowired
    private SkillValidation skillValidation;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Skill findOne(long skillId) throws SkillNotFoundException {
        Optional<Skill> skill = skillRepository.findById(skillId);

        if(!skill.isPresent())
            throw new SkillNotFoundException(skillId);

        return skill.get();
    }

    @Override
    public Skill create(Skill skill) throws SkillValidationException, SkillIsExistException, CategoryNotFoundException {
        skillValidation.validate(skill, GroupCreateSkill.class);

        Optional<Category> categoryOptional = categoryRepository.findById(skill.getCategoryId());

        if(!categoryOptional.isPresent())
            throw new CategoryNotFoundException(skill.getCategoryId());

        if(skillRepository.getByNameAndCategoryId(skill.getName(), skill.getCategoryId()) != null)
            throw new SkillIsExistException(skill.getName(), skill.getCategoryId());

        skill.setCreatedAt(new Date());
        skill.setUpdatedAt(new Date());
        skill = skillRepository.save(skill);
        skill.setCategory(categoryOptional.get());

        return skill;
    }

    @Override
    public Skill update(long skillId, Skill skill) throws SkillValidationException, SkillIsExistException, SkillNotFoundException, CategoryNotFoundException {
        skillValidation.validate(skill, GroupUpdateSkill.class);

        Optional<Category> categoryOptional = categoryRepository.findById(skill.getCategoryId());

        if(!categoryOptional.isPresent())
            throw new CategoryNotFoundException(skill.getCategoryId());

        Optional<Skill> optionalSkill = skillRepository.findById(skillId);

        if (!optionalSkill.isPresent())
            throw new SkillNotFoundException(skillId, skill.getCategoryId());

        Skill oldSkill = optionalSkill.get();

        Skill skillTemp = skillRepository.getByNameAndCategoryId(skill.getName(), skill.getCategoryId());
        if((skillTemp != null) && (skillTemp.getId() != skillId))
            throw new SkillIsExistException(skill.getName(), skill.getCategoryId());

        oldSkill.setName(skill.getName());
        oldSkill.setCategoryId(skill.getCategoryId());
        oldSkill.setUpdatedAt(new Date());

        oldSkill = skillRepository.save(oldSkill);
        oldSkill.setCategory(categoryOptional.get());

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
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        if(!categoryOptional.isPresent())
            throw new CategoryNotFoundException(categoryId);

        skill.setCategoryId(categoryId);
        skillValidation.validate(skill, GroupCreateSkill.class);

        if(skillRepository.getByNameAndCategoryId(skill.getName(), categoryId) != null)
            throw new SkillIsExistException(skill.getName());

        skill.setCreatedAt(new Date());
        skill.setUpdatedAt(new Date());

        skill = skillRepository.save(skill);
        skill.setCategory(categoryOptional.get());

        return skill;
    }

    @Override
    public Skill updateByCategoryIdAndSkillId(Skill skill, long categoryId, long skillId) throws SkillNotFoundException, SkillValidationException, SkillIsExistException {
        Skill oldSkill = skillRepository.getByIdAndCategoryId(skillId, categoryId);

        if(oldSkill == null)
            throw new SkillNotFoundException(skillId, categoryId);

        skill.setCategoryId(categoryId);
        skillValidation.validate(skill, GroupUpdateSkill.class);

        if(skill.getName() != null &&
           !oldSkill.getName().equals(skill.getName())
           && skillRepository.getByNameAndCategoryId(skill.getName(), categoryId) != null)
            throw new SkillIsExistException(skill.getName());

        oldSkill.setName(skill.getName());
        oldSkill.setUpdatedAt(new Date());

        skillValidation.validate(skill, Default.class);

        return skillRepository.save(oldSkill);
    }

    @Override
    public void deleteByCategoryIdAndSkillId(long categoryId, long skillId) throws SkillNotFoundException {

        if(!skillRepository.existsByIdAndCategoryId(skillId, categoryId))
            throw new SkillNotFoundException(skillId, categoryId);

        skillRepository.deleteByIdAndCategoryId(skillId, categoryId);
    }
}

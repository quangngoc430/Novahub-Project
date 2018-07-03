package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.CategoryNotFoundException;
import vn.novahub.helpdesk.exception.SkillIsExistException;
import vn.novahub.helpdesk.exception.SkillNotFoundException;
import vn.novahub.helpdesk.exception.SkillValidationException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.AccountHasSkill;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.repository.AccountHasSkillRepository;
import vn.novahub.helpdesk.repository.CategoryRepository;
import vn.novahub.helpdesk.repository.SkillRepository;
import vn.novahub.helpdesk.validation.SkillValidation;

import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;

@Service
public class SkillServiceImpl implements SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private AccountHasSkillRepository accountHasSkillRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SkillValidation skillValidation;

    @Override
    public Skill create(Skill skill, HttpServletRequest request) throws SkillValidationException {

        skillValidation.validate(skill, Default.class);

        Account accountLogin = accountService.getAccountLogin();

        Skill oldSkill = skillRepository.findByName(skill.getName());

        if(oldSkill == null) {
            skill.setName(skill.getName().toLowerCase());
            return skillRepository.save(skill);
        } else {
            AccountHasSkill accountHasSkill = new AccountHasSkill();
            accountHasSkill.setAccountId(accountLogin.getId());
            accountHasSkill.setSkillId(oldSkill.getId());
            accountHasSkillRepository.save(accountHasSkill);
        }

        return skill;
    }

    @Override
    public Skill createByCategoryId(Skill skill, long categoryId) throws SkillValidationException, SkillIsExistException {

        skillValidation.validate(skill, Default.class);

        if(skillRepository.existsByName(skill.getName()))
            throw new SkillIsExistException(skill.getName());

        skill.setCategoryId(categoryId);

        skill = skillRepository.save(skill);
        skill.setCategory(categoryRepository.getById(categoryId));

        return skill;
    }

    @Override
    public Skill updateBySkillId(Skill skill, long skillId, HttpServletRequest request) {
        Account accountLogin = accountService.getAccountLogin();

        Skill oldSkill = skillRepository.findByName(skill.getName());

        if(oldSkill == null){
            skill.setName(skill.getName());
            Skill newSkill = skillRepository.save(skill);

            AccountHasSkill accountHasSkill = accountHasSkillRepository.findByAccountIdAndSkillId(accountLogin.getId(), skillId);
            accountHasSkill.setSkillId(newSkill.getId());
            accountHasSkillRepository.save(accountHasSkill);

            return newSkill;
        } else {
            AccountHasSkill accountHasSkill = accountHasSkillRepository.findByAccountIdAndSkillId(accountLogin.getId(), skillId);
            accountHasSkill.setSkillId(oldSkill.getId());
            accountHasSkillRepository.save(accountHasSkill);

            skillRepository.deleteById(skillId);

            return oldSkill;
        }
    }

    @Override
    public Skill updateByCategoryIdAndSkillId(Skill skill, long categoryId, long skillId) throws SkillNotFoundException, SkillValidationException, SkillIsExistException {

        skillValidation.validate(skill, Default.class);

        Skill oldSkill = skillRepository.getByIdAndCategoryId(skillId, categoryId);

        if(oldSkill == null)
            throw new SkillNotFoundException(skillId, categoryId);

        if(skillRepository.existsByName(skill.getName()))
            throw new SkillIsExistException(skill.getName());

        oldSkill.setName(skill.getName());

        return skillRepository.save(oldSkill);
    }

    @Override
    public Skill getBySkillId(long skillId) throws SkillNotFoundException {
        Skill skill = skillRepository.getById(skillId);

        if(skill == null)
            throw new SkillNotFoundException(skillId);

        return skill;
    }

    @Override
    public Skill getByCategoryIdAndSkillId(long categoryId, long skillId) throws SkillNotFoundException {
        Skill skill = skillRepository.getByIdAndCategoryId(skillId, categoryId);

        if(skill == null)
            throw new SkillNotFoundException(skillId, categoryId);

        return skill;
    }

    @Override
    public void deleteByCategoryIdAndSkillId(long categoryId, long skillId) throws SkillNotFoundException {

        if(!skillRepository.existsByIdAndCategoryId(skillId, categoryId))
            throw new SkillNotFoundException(skillId, categoryId);

        skillRepository.deleteByIdAndCategoryId(skillId, categoryId);
    }

    @Override
    public Page<Skill> getAllByCategoryIdAndName(long categoryId, String name, Pageable pageable) throws CategoryNotFoundException {
        if(!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException(categoryId);

        return skillRepository.getAllByCategoryIdAndNameLike(categoryId, "%" + name + "%", pageable);
    }

    @Override
    public Page<Skill> getAllByName(String nameSkill, Pageable pageable, HttpServletRequest request) {
        nameSkill = "%" + nameSkill + "%";

        Account accountLogin = accountService.getAccountLogin();

        return skillRepository.getAllSkillsByAccountIdAndNameLike(accountLogin.getId(), nameSkill, pageable);
    }
}

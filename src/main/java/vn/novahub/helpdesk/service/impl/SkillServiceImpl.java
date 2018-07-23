package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.CategoryNotFoundException;
import vn.novahub.helpdesk.exception.SkillNotFoundException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.AccountHasSkill;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.repository.AccountHasSkillRepository;
import vn.novahub.helpdesk.repository.CategoryRepository;
import vn.novahub.helpdesk.repository.SkillRepository;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.SkillService;

import javax.servlet.http.HttpServletRequest;

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

    @Override
    public Skill create(Skill skill, HttpServletRequest request) {

        Account accountLogin = accountService.getAccountLogin();

        Skill oldSkill = skillRepository.getByName(skill.getName().toLowerCase());

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
    public Skill createByCategoryId(Skill skill, long categoryId) {
        Skill oldSkill = skillRepository.getByName(skill.getName());

        if(oldSkill != null){
            return oldSkill;
        }

        skill.setName(skill.getName().toLowerCase());
        skill.setCategoryId(categoryId);

        return skillRepository.save(skill);
    }

    @Override
    public Skill updateBySkillId(Skill skill, long skillId, HttpServletRequest request) {
        Account accountLogin = accountService.getAccountLogin();

        Skill oldSkill = skillRepository.getByName(skill.getName().toLowerCase());

        if(oldSkill == null){
            skill.setName(skill.getName().toLowerCase());
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
    public Skill updateByCategoryIdAndSkillId(Skill skill, long categoryId, long skillId) throws CategoryNotFoundException, SkillNotFoundException {
        if(!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException(categoryId);

        Skill oldSkill = skillRepository.getByIdAndCategoryId(skillId, categoryId);

        if(oldSkill == null)
            throw new SkillNotFoundException(skillId);

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
    public Skill getByCategoryIdAndSkillId(long categoryId, long skillId) {
        return skillRepository.getByIdAndCategoryId(skillId, categoryId);
    }

    @Override
    public void deteleByCategoryIdAndSkillId(long categoryId, long skillId) throws CategoryNotFoundException, SkillNotFoundException {

        if(!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException(categoryId);

        if(!skillRepository.existsByIdAndCategoryId(skillId, categoryId))
            throw new SkillNotFoundException(skillId);

        skillRepository.deleteById(skillId);
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

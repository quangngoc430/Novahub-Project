package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.AccountHasSkill;
import vn.novahub.helpdesk.model.Level;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.repository.*;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.AccountSkillService;
import vn.novahub.helpdesk.validation.GroupCreateSkill;
import vn.novahub.helpdesk.validation.GroupUpdateSkill;
import vn.novahub.helpdesk.validation.LevelValidation;
import vn.novahub.helpdesk.validation.SkillValidation;

import java.util.Date;

@Service
public class AccountSkillServiceImpl implements AccountSkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillValidation skillValidation;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountHasSkillRepository accountHasSkillRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LevelValidation levelValidation;

    @Autowired
    private LevelRepository levelRepository;

    @Override
    public Skill findOne(long skillId) throws SkillNotFoundException {
        Skill skill = skillRepository.getById(skillId);

        if(skill == null)
            throw new SkillNotFoundException(skillId);

        return skill;
    }

    @Override
    public Page<Account> getAllUsersBySkillId(long skillId, Pageable pageable) throws SkillNotFoundException {

        if(!skillRepository.existsById(skillId))
            throw new SkillNotFoundException(skillId);

        return accountRepository.getAllBySkillId(skillId, pageable);
    }

    @Override
    public Page<Skill> getAllByKeyword(String keyword, Pageable pageable) {
        return skillRepository.getAllByNameContaining("%" + keyword + "%", pageable);
    }


    @Override
    public Page<Skill> getAllByKeywordForAccountLogin(String keyword, Pageable pageable) {
        Account accountLogin = accountService.getAccountLogin();

        return skillRepository.getAllByNameContainingAndAccountId("%" + keyword + "%", accountLogin.getId(), pageable);
    }

    @Override
    public Skill create(Skill newSkill) throws SkillValidationException, SkillIsExistException, CategoryNotFoundException {

        skillValidation.validate(newSkill, GroupCreateSkill.class);

        if(newSkill.getCategory() != null && !categoryRepository.existsById(newSkill.getCategoryId()))
            throw new CategoryNotFoundException(newSkill.getCategoryId());

        Account accountLogin = accountService.getAccountLogin();

        // check skill is exists
        Skill oldSkill = skillRepository.getByName(newSkill.getName());

        if(oldSkill == null) {

            newSkill.setCreatedAt(new Date());
            newSkill.setUpdatedAt(new Date());
            newSkill = skillRepository.save(newSkill);

        } else {

            newSkill.setId(oldSkill.getId());
            newSkill.setCategoryId(oldSkill.getCategoryId());

            if(accountHasSkillRepository.existsByAccountIdAndSkillId(accountLogin.getId(), newSkill.getId()))
                throw new SkillIsExistException(newSkill.getName());

        }

        Level newLevel = new Level();
        newLevel.setValue(newSkill.getLevel());
        newLevel.setAccountId(accountLogin.getId());
        newLevel.setSkillId(newSkill.getId());
        newLevel.setCreatedAt(new Date());
        newLevel.setUpdatedAt(new Date());
        levelRepository.save(newLevel);

        AccountHasSkill accountHasSkill = new AccountHasSkill();
        accountHasSkill.setAccountId(accountLogin.getId());
        accountHasSkill.setSkillId(newSkill.getId());
        accountHasSkillRepository.save(accountHasSkill);

        newSkill.setCategory(categoryRepository.getById(newSkill.getCategoryId()));

        return newSkill;
    }

    @Override
    public Skill update(long skillId, Skill newSkill) throws SkillValidationException, SkillNotFoundException {

        skillValidation.validate(newSkill, GroupUpdateSkill.class);

        Account accountLogin = accountService.getAccountLogin();

        Skill oldSkill = skillRepository.getByAccountIdAndSkillId(accountLogin.getId(), skillId);

        if(oldSkill == null)
            throw new SkillNotFoundException(skillId);

        Level oldLevel = levelRepository.getByAccountIdAndSkillId(accountLogin.getId(), skillId);
        oldLevel.setValue(newSkill.getLevel());
        levelRepository.save(oldLevel);

        oldSkill.setLevel(newSkill.getLevel());

        return oldSkill;
    }

    @Override
    public void delete(long skillId) throws SkillNotFoundException {
        Account accountLogin = accountService.getAccountLogin();

        if(skillRepository.getByAccountIdAndSkillId(accountLogin.getId(), skillId) == null)
            throw new SkillNotFoundException(skillId);

        accountHasSkillRepository.deleteByAccountIdAndSkillId(accountLogin.getId(), skillId);

        // check count of skill has skillId, if it equals 1 then delete its
        if(accountHasSkillRepository.countBySkillId(skillId) == 1) {
            skillRepository.deleteById(skillId);
        }

    }

    @Override
    public Skill getByCategoryIdAndSkillId(long categoryId, long skillId) throws SkillNotFoundException {
        Skill skill = skillRepository.getByIdAndCategoryId(skillId, categoryId);

        if(skill == null)
            throw new SkillNotFoundException(skillId, categoryId);

        return skill;
    }

    @Override
    public Page<Skill> getAllByCategoryIdAndName(long categoryId, String name, Pageable pageable) throws CategoryNotFoundException {
        if(!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException(categoryId);

        return skillRepository.getAllByCategoryIdAndNameContaining(categoryId, "%" + name + "%", pageable);
    }

    @Override
    public Page<Skill> getAllByAccountId(long accountId, Pageable pageable) throws AccountNotFoundException {
        if(!accountRepository.existsById(accountId))
            throw new AccountNotFoundException(accountId);

        return skillRepository.getAllByAccountId(accountId, pageable);
    }

}

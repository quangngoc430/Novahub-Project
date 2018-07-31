package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.*;
import vn.novahub.helpdesk.repository.*;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.AccountSkillService;
import vn.novahub.helpdesk.validation.GroupCreateSkill;
import vn.novahub.helpdesk.validation.GroupUpdateSkill;
import vn.novahub.helpdesk.validation.LevelValidation;
import vn.novahub.helpdesk.validation.SkillValidation;

import java.util.Date;
import java.util.Optional;

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
        Account accountLogin = accountService.getAccountLogin();

        Skill skill = skillRepository.getByAccountIdAndSkillId(accountLogin.getId(), skillId);

        if(skill == null)
            throw new SkillNotFoundException(skillId);

        skill.setLevel(levelRepository.getByAccountIdAndSkillId(accountLogin.getId(), skillId));

        return skill;
    }

    @Override
    public Page<Account> getAllUsersBySkillId(long skillId, Pageable pageable) throws SkillNotFoundException {

        if(!skillRepository.existsById(skillId))
            throw new SkillNotFoundException(skillId);

        return accountRepository.getAllBySkillId(skillId, pageable);
    }

    @Override
    public Page<Skill> getAllByKeyword(long categoryId, String keyword, Pageable pageable) throws CategoryNotFoundException {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        if(!categoryOptional.isPresent())
            throw new CategoryNotFoundException(categoryId);

        return skillRepository.getAllByCategoryIdAndNameContaining(categoryId, keyword, pageable);
    }


    @Override
    public Page<Skill> getAllByKeywordForAccountLogin(long categoryId, String keyword, Pageable pageable) throws CategoryNotFoundException {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        if(!categoryOptional.isPresent())
            throw new CategoryNotFoundException(categoryId);

        Account accountLogin = accountService.getAccountLogin();

        Page<Skill> skills = skillRepository.getAllByNameContainingAndAccountIdAndCategoryId(keyword, accountLogin.getId(), categoryId, pageable);

        for(Skill skill : skills) {
            skill.setLevel(levelRepository.getByAccountIdAndSkillId(accountLogin.getId(), skill.getId()));
        }

        return skills;
    }

    @Override
    public Skill create(Skill newSkill) throws SkillValidationException, SkillIsExistException, CategoryNotFoundException, LevelValidationException {

        skillValidation.validate(newSkill, GroupCreateSkill.class);
        levelValidation.validate(newSkill.getLevel(), GroupCreateSkill.class);

        if(!categoryRepository.existsById(newSkill.getCategoryId()))
            throw new CategoryNotFoundException(newSkill.getCategoryId());

        Account accountLogin = accountService.getAccountLogin();

        // check skill is exists
        Skill oldSkill = skillRepository.getByNameAndCategoryId(newSkill.getName(), newSkill.getCategoryId());

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
        newLevel.setValue(newSkill.getLevel().getValue());
        newLevel.setAccountId(accountLogin.getId());
        newLevel.setSkillId(newSkill.getId());
        newLevel.setCreatedAt(new Date());
        newLevel.setUpdatedAt(new Date());
        newLevel = levelRepository.save(newLevel);
        newSkill.setLevel(newLevel);

        AccountHasSkill accountHasSkill = new AccountHasSkill();
        accountHasSkill.setAccountId(accountLogin.getId());
        accountHasSkill.setSkillId(newSkill.getId());
        accountHasSkill.setCreatedAt(new Date());
        accountHasSkill.setUpdatedAt(new Date());
        accountHasSkillRepository.save(accountHasSkill);

        newSkill.setCategory(categoryRepository.getById(newSkill.getCategoryId()));

        return newSkill;
    }

    @Override
    public Skill update(long skillId, Skill newSkill) throws SkillNotFoundException, LevelValidationException, SkillValidationException, SkillIsExistException {

        skillValidation.validate(newSkill, GroupUpdateSkill.class);
        levelValidation.validate(newSkill.getLevel(), GroupUpdateSkill.class);

        Account accountLogin = accountService.getAccountLogin();

        Skill oldSkill = skillRepository.getByAccountIdAndSkillId(accountLogin.getId(), skillId);

        if(oldSkill == null)
            throw new SkillNotFoundException(skillId);

        // have same skillName and categoryId
        if((oldSkill.getCategoryId() == newSkill.getCategoryId()) && (oldSkill.getName().equals(newSkill.getName()))) {
            Level oldLevel = levelRepository.getByAccountIdAndSkillId(accountLogin.getId(), skillId);
            oldLevel.setValue(newSkill.getLevel().getValue());
            oldLevel.setUpdatedAt(new Date());
            oldLevel = levelRepository.save(oldLevel);
            oldSkill.setLevel(oldLevel);

            return oldSkill;
        }

        // check skill is exist
        Skill skillTemp = skillRepository.getByNameAndCategoryId(newSkill.getName(), newSkill.getCategoryId());

        // skill is exist
        if (skillTemp != null) {
            if(accountHasSkillRepository.getByAccountIdAndSkillId(accountLogin.getId(), skillTemp.getId()) != null)
                throw new SkillIsExistException(skillTemp.getName(), accountLogin.getId(), skillTemp.getCategoryId());

            AccountHasSkill accountHasSkill = accountHasSkillRepository.getByAccountIdAndSkillId(accountLogin.getId(), skillId);
            accountHasSkill.setSkillId(skillTemp.getId());
            accountHasSkillRepository.save(accountHasSkill);

            Level level = levelRepository.getByAccountIdAndSkillId(accountLogin.getId(), skillId);
            level.setSkillId(skillTemp.getId());
            level.setValue(newSkill.getLevel().getValue());
            skillTemp.setLevel(levelRepository.save(level));

            return skillTemp;
        } else {
            newSkill.setCreatedAt(new Date());
            newSkill.setUpdatedAt(new Date());
            skillTemp = skillRepository.save(newSkill);

            AccountHasSkill accountHasSkill = accountHasSkillRepository.getByAccountIdAndSkillId(accountLogin.getId(), skillId);
            accountHasSkill.setSkillId(newSkill.getId());
            accountHasSkillRepository.save(accountHasSkill);

            Level level = levelRepository.getByAccountIdAndSkillId(accountLogin.getId(), skillId);
            level.setSkillId(newSkill.getId());
            level.setValue(newSkill.getLevel().getValue());
            skillTemp.setLevel(levelRepository.save(level));

            // delete skill if it doesn't belong to any users
            if (accountHasSkillRepository.countBySkillId(skillId) == 0) {
               skillRepository.deleteById(skillId);
            }

            return skillTemp;
        }
    }

    @Override
    public void delete(long skillId) throws SkillNotFoundException {
        Account accountLogin = accountService.getAccountLogin();

        Optional<Skill> skillOptional = skillRepository.findById(skillId);

        if(!skillOptional.isPresent())
            throw new SkillNotFoundException(skillId);

        accountHasSkillRepository.deleteByAccountIdAndSkillId(accountLogin.getId(), skillId);
        levelRepository.deleteByAccountIdAndSkillId(accountLogin.getId(), skillId);
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
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        if(!categoryOptional.isPresent())
            throw new CategoryNotFoundException(categoryId);

        Page<Skill> skills = skillRepository.getAllByCategoryIdAndNameContaining(categoryId, name, pageable);

        for(Skill skill : skills) {
            skill.setLevel(null);
        }

        return skills;
    }

    @Override
    public Page<Skill> getAllByAccountId(long accountId, Pageable pageable) throws AccountNotFoundException {
        Optional<Account> accountOptional = accountRepository.findById(accountId);

        if(!accountOptional.isPresent())
            throw new AccountNotFoundException(accountId);

        Page<Skill> skills = skillRepository.getAllByAccountId(accountId, pageable);

        for(Skill skill : skills) {
            skill.setLevel(levelRepository.getBySkillId(skill.getId()));
        }

        return skills;
    }

}

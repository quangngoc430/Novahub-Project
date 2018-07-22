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
import vn.novahub.helpdesk.repository.AccountRepository;
import vn.novahub.helpdesk.repository.CategoryRepository;
import vn.novahub.helpdesk.repository.SkillRepository;
import vn.novahub.helpdesk.validation.GroupCreateSkill;
import vn.novahub.helpdesk.validation.GroupUpdateSkill;
import vn.novahub.helpdesk.validation.SkillValidation;

import java.util.Date;

@Service
public class AccountSkillServiceImpl implements AccountSkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillValidation skillValidation;

    @Autowired
    private  AccountService accountService;

    @Autowired
    private AccountHasSkillRepository accountHasSkillRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AccountRepository accountRepository;

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
        return skillRepository.getAllByNameLike("%" + keyword + "%", pageable);
    }


    @Override
    public Page<Skill> getAllByKeywordForAccountLogin(String keyword, Pageable pageable) {
        Account accountLogin = accountService.getAccountLogin();

        return skillRepository.getAllByNameLikeAndAccountId("%" + keyword + "%", accountLogin.getId(), pageable);
    }

    @Override
    public Skill create(Skill skill) throws SkillValidationException, SkillIsExistException {

        skillValidation.validate(skill, GroupCreateSkill.class);

        Account accountLogin = accountService.getAccountLogin();

        Skill oldSkill = skillRepository.getByNameAndLevelAndCategoryId(skill.getName(), skill.getLevel(), skill.getCategoryId());

        // skill is exist
        if(oldSkill != null){

            // check account has skill
            if(!accountHasSkillRepository.existsByAccountIdAndSkillId(accountLogin.getId(), oldSkill.getId())) {
                AccountHasSkill accountHasSkill = new AccountHasSkill();
                accountHasSkill.setAccountId(accountLogin.getId());
                accountHasSkill.setSkillId(oldSkill.getId());
                accountHasSkillRepository.save(accountHasSkill);
                return oldSkill;
            } else {
                throw new SkillIsExistException(skill.getName(), skill.getLevel(), skill.getCategoryId());
            }
        }

        skill.setCreatedAt(new Date());
        skill.setUpdatedAt(new Date());
        skill = skillRepository.save(skill);
        AccountHasSkill accountHasSkill = new AccountHasSkill();
        accountHasSkill.setAccountId(accountLogin.getId());
        accountHasSkill.setSkillId(skill.getId());
        accountHasSkillRepository.save(accountHasSkill);

        skill.setCategory(categoryRepository.getById(skill.getCategoryId()));

        return skill;
    }

    @Override
    public Skill update(long skillId, Skill skill) throws SkillValidationException, SkillNotFoundException {

        Account accountLogin = accountService.getAccountLogin();

        Skill oldSkill = skillRepository.getByAccountIdAndSkillId(accountLogin.getId(), skillId);

        if(oldSkill == null)
            throw new SkillNotFoundException(skillId);

        skillValidation.validate(skill, GroupUpdateSkill.class);

        // check changing of data between input with oldSkill
        if(skill.equals(oldSkill))
            return oldSkill;

        accountHasSkillRepository.deleteByAccountIdAndSkillId(accountLogin.getId(), skillId);

        // check count of oldSkill, if it equals 1 then delete its
        if(accountHasSkillRepository.countBySkillId(oldSkill.getId()) == 1) {
            skillRepository.deleteById(oldSkill.getId());
        }

        // get skill that have same data with input skill
        Skill skillSameInput = skillRepository.getByNameAndLevelAndCategoryId(skill.getName(), skill.getLevel(), skill.getCategoryId());

        // skill have same data that found
        if(skillSameInput != null) {
            AccountHasSkill newAccountHasSkill = new AccountHasSkill();
            newAccountHasSkill.setSkillId(skillSameInput.getId());
            newAccountHasSkill.setAccountId(accountLogin.getId());
            accountHasSkillRepository.save(newAccountHasSkill);

            return skillSameInput;
        }

        // skill have same data that isn't found
        // create new skill
        skill.setCreatedAt(new Date());
        skill.setUpdatedAt(new Date());
        skill = skillRepository.save(skill);

        AccountHasSkill newAccountHasSkill = new AccountHasSkill();
        newAccountHasSkill.setSkillId(skill.getId());
        newAccountHasSkill.setAccountId(accountLogin.getId());
        accountHasSkillRepository.save(newAccountHasSkill);

        skill.setCategory(categoryRepository.getById(skill.getCategoryId()));

        return skill;
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

        return skillRepository.getAllByCategoryIdAndNameLike(categoryId, "%" + name + "%", pageable);
    }

}

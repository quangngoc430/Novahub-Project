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
<<<<<<< HEAD
import vn.novahub.helpdesk.validation.SkillValidation;

import javax.validation.groups.Default;
=======
import vn.novahub.helpdesk.validation.GroupCreateSkill;
import vn.novahub.helpdesk.validation.GroupUpdateSkill;
import vn.novahub.helpdesk.validation.SkillValidation;

import javax.validation.groups.Default;
import java.util.Date;
>>>>>>> develop

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
<<<<<<< HEAD
    private AccountRepository accountRepository;

    @Autowired
    private SkillValidation skillValidation;

    @Override
    public Skill getById(long skillId) throws SkillNotFoundException {
        Skill skill = skillRepository.getById(skillId);

        if(skill == null)
            throw new SkillNotFoundException(skillId);

        return skill;
    }

    @Override
    public Page<Skill> getAllByKeyword(String keyword, Pageable pageable) {
        keyword = "%" + keyword + "%";

        return skillRepository.getAllByNameLike(keyword, pageable);
    }

    @Override
    public Skill createByAdmin(Skill skill) throws SkillValidationException, SkillIsExistException {

        skillValidation.validate(skill, Default.class);

        Skill oldSkill = skillRepository.getByNameAndCategoryId(skill.getName(), skill.getCategoryId());

        if(oldSkill != null) {
            return oldSkill;
=======
    private SkillValidation skillValidation;

    @Override
    public Skill create(Skill skill) throws SkillValidationException {

        skillValidation.validate(skill, Default.class);

        Account accountLogin = accountService.getAccountLogin();

        Skill oldSkill = skillRepository.getByName(skill.getName());

        if(oldSkill == null) {
            skill.setName(skill.getName().toLowerCase());
            return skillRepository.save(skill);
        } else {
            AccountHasSkill accountHasSkill = new AccountHasSkill();
            accountHasSkill.setAccountId(accountLogin.getId());
            accountHasSkill.setSkillId(oldSkill.getId());
            accountHasSkillRepository.save(accountHasSkill);
>>>>>>> develop
        }

        skill = skillRepository.save(skill);
        skill.setCategory(categoryRepository.getById(skill.getCategoryId()));

        return skill;
    }

    @Override
<<<<<<< HEAD
    public Skill updateByAdmin(long skillId, Skill skill) throws SkillValidationException, SkillIsExistException, SkillNotFoundException {
        skillValidation.validate(skill, Default.class);

        Skill oldSkill = skillRepository.getById(skillId);

        if (oldSkill == null) {
            throw new SkillNotFoundException(skillId);
        }

        if ((oldSkill.getName().equals(skill.getName())) && (oldSkill.getCategoryId() == skill.getCategoryId())){
            return oldSkill;
        }

        oldSkill.setName(skill.getName());
        oldSkill.setCategoryId(skill.getCategoryId());

        oldSkill = skillRepository.save(oldSkill);
        oldSkill.setCategory(categoryRepository.getById(oldSkill.getCategoryId()));

        return oldSkill;
    }

    @Override
    public void deleteByAdmin(long skillId) throws SkillNotFoundException {

        if(!skillRepository.existsById(skillId))
            throw new SkillNotFoundException(skillId);

        skillRepository.deleteById(skillId);
    }

    @Override
    public Page<Account> getAllUsersOfASkill(long skillId, Pageable pageable) throws SkillNotFoundException {

        if(!skillRepository.existsById(skillId))
            throw new SkillNotFoundException(skillId);

        return accountRepository.getAllBySkillId(skillId, pageable);
    }
=======
    public Skill createByCategoryId(Skill skill, long categoryId) throws SkillValidationException, SkillIsExistException, CategoryNotFoundException {

        if(!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException(categoryId);

        skill.setCategoryId(categoryId);
        skillValidation.validate(skill, GroupCreateSkill.class);

        if(skillRepository.existsByNameAndLevel(skill.getName(), skill.getLevel()))
            throw new SkillIsExistException(skill.getName());

        skill.setCategoryId(categoryId);
        skill.setCreatedAt(new Date());
        skill.setUpdatedAt(new Date());

        skill = skillRepository.save(skill);
        skill.setCategory(categoryRepository.getById(categoryId));

        return skill;
    }

    @Override
    public Skill updateBySkillId(Skill skill, long skillId) {
        Account accountLogin = accountService.getAccountLogin();

        Skill oldSkill = skillRepository.getByName(skill.getName());

        if(oldSkill == null){
            skill.setName(skill.getName());
            Skill newSkill = skillRepository.save(skill);
>>>>>>> develop

    @Override
    public Page<Skill> getAllByKeywordForAccountLogin(String keyword, Pageable pageable) {
        Account accountLogin = accountService.getAccountLogin();

        keyword = "%" + keyword + "%";

        return skillRepository.getAllByNameLikeAndAccountId(keyword, accountLogin.getId(), pageable);
    }

    @Override
    public Skill createForAccountLogin(Skill skill) throws SkillValidationException, SkillIsExistException {

        skillValidation.validate(skill, Default.class);

        Account accountLogin = accountService.getAccountLogin();

        Skill oldSkill = skillRepository.getByNameAndCategoryId(skill.getName(), skill.getCategoryId());

        // skill is exist
        if(oldSkill != null){

            // check account has skill
            if(!accountHasSkillRepository.existsByAccountIdAndSkillId(accountLogin.getId(), oldSkill.getId())) {
                AccountHasSkill accountHasSkill = new AccountHasSkill();
                accountHasSkill.setAccountId(accountLogin.getId());
                accountHasSkill.setSkillId(oldSkill.getId());
                accountHasSkillRepository.save(accountHasSkill);
            }

            return oldSkill;
        }

        skill = skillRepository.save(skill);
        AccountHasSkill accountHasSkill = new AccountHasSkill();
        accountHasSkill.setAccountId(accountLogin.getId());
        accountHasSkill.setSkillId(skill.getId());
        accountHasSkillRepository.save(accountHasSkill);

        skill.setCategory(categoryRepository.getById(skill.getCategoryId()));

        return skill;
    }

    @Override
<<<<<<< HEAD
    public Skill updateForAccountLogin(long skillId, Skill skill) throws SkillValidationException, SkillNotFoundException {

        skillValidation.validate(skill, Default.class);

        Account accountLogin = accountService.getAccountLogin();

        Skill oldSkill = skillRepository.getByAccountIdAndSkillId(accountLogin.getId(), skillId);
=======
    public Skill updateByCategoryIdAndSkillId(Skill skill, long categoryId, long skillId) throws SkillNotFoundException, SkillValidationException, SkillIsExistException {

        Skill oldSkill = skillRepository.getByIdAndCategoryId(skillId, categoryId);
>>>>>>> develop

        if(oldSkill == null)
            throw new SkillNotFoundException(skillId, categoryId);

        skill.setCategoryId(categoryId);
        skillValidation.validate(skill, GroupUpdateSkill.class);

        if(!oldSkill.equals(skill) && skillRepository.existsByNameAndLevel(skill.getName(), skill.getLevel()))
            throw new SkillIsExistException(skill.getName());

<<<<<<< HEAD
        // check changing of data between input with oldSkill
        if(skill.getName().equals(oldSkill.getName()) && (skill.getCategoryId() == oldSkill.getCategoryId()))
            return oldSkill;

        // check count of oldSkill, if it equals 1 then delete its
        if(accountHasSkillRepository.countBySkillId(oldSkill.getId()) == 1) {
            accountHasSkillRepository.deleteBySkillId(oldSkill.getId());
            skillRepository.deleteById(oldSkill.getId());
        }

        // get skill that have same data with input skill
        Skill skillSameInput = skillRepository.getByNameAndCategoryId(skill.getName(), skill.getCategoryId());

        // skill have same data that found
        if(skillSameInput != null) {
            accountHasSkillRepository.deleteByAccountIdAndSkillId(accountLogin.getId(), skillId);
            AccountHasSkill newAccountHasSkill = new AccountHasSkill();
            newAccountHasSkill.setSkillId(skillSameInput.getId());
            newAccountHasSkill.setAccountId(accountLogin.getId());
            accountHasSkillRepository.save(newAccountHasSkill);

            return skillSameInput;
        }

        // skill have same data that isn't found
        skill = skillRepository.save(skill);
        accountHasSkillRepository.deleteByAccountIdAndSkillId(accountLogin.getId(), skillId);
        AccountHasSkill newAccountHasSkill = new AccountHasSkill();
        newAccountHasSkill.setSkillId(skill.getId());
        newAccountHasSkill.setAccountId(accountLogin.getId());
        accountHasSkillRepository.save(newAccountHasSkill);

        skill.setCategory(categoryRepository.getById(skill.getCategoryId()));

        return skill;
    }

    @Override
    public void deleteForAccountLogin(long skillId) throws SkillNotFoundException {
=======
        oldSkill.setName(skill.getName());
        oldSkill.setLevel(skill.getLevel());
        oldSkill.setUpdatedAt(new Date());

        return skillRepository.save(oldSkill);
    }

    @Override
    public Skill getBySkillId(long skillId) throws SkillNotFoundException {
        Skill skill = skillRepository.getById(skillId);
>>>>>>> develop

        Account accountLogin = accountService.getAccountLogin();

        if(skillRepository.getByAccountIdAndSkillId(accountLogin.getId(), skillId) == null)
            throw new SkillNotFoundException(accountLogin.getId(), skillId);

        // check count of skill has skillId, if it equals 1 then delete its
        if(accountHasSkillRepository.countBySkillId(skillId) == 1) {
            accountHasSkillRepository.deleteBySkillId(skillId);
            skillRepository.deleteById(skillId);
        }

        accountHasSkillRepository.deleteByAccountIdAndSkillId(accountLogin.getId(), skillId);
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
<<<<<<< HEAD
    public Skill getByCategoryIdAndSkillId(long categoryId, long skillId) {
        return skillRepository.getByIdAndCategoryId(skillId, categoryId);
    }

    @Override
    public void deleteByCategoryIdAndSkillId(long categoryId, long skillId) throws CategoryNotFoundException, SkillNotFoundException {

        if(!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException(categoryId);
=======
    public Skill getByCategoryIdAndSkillId(long categoryId, long skillId) throws SkillNotFoundException {
        Skill skill = skillRepository.getByIdAndCategoryId(skillId, categoryId);

        if(skill == null)
            throw new SkillNotFoundException(skillId, categoryId);

        return skill;
    }

    @Override
    public void deleteByCategoryIdAndSkillId(long categoryId, long skillId) throws SkillNotFoundException {
>>>>>>> develop

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

<<<<<<< HEAD
=======
    @Override
    public Page<Skill> getAllByName(String nameSkill, Pageable pageable) {
        nameSkill = "%" + nameSkill + "%";

        Account accountLogin = accountService.getAccountLogin();

        return skillRepository.getAllSkillsByAccountIdAndNameLike(accountLogin.getId(), nameSkill, pageable);
    }
>>>>>>> develop
}

package vn.novahub.helpdesk.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.*;
import vn.novahub.helpdesk.repository.*;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.AccountSkillService;
import vn.novahub.helpdesk.validation.GroupUpdateSkillWithLevel;
import vn.novahub.helpdesk.validation.SkillValidation;

import java.util.*;

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

    @Override
    public Skill findOne(long skillId) throws SkillNotFoundException {
        Account accountLogin = accountService.getAccountLogin();

        Skill skill = skillRepository.getByAccountIdAndSkillId(accountLogin.getId(), skillId);

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
    public Page<Skill> getAllByKeyword(long categoryId, String keyword, Pageable pageable) throws CategoryNotFoundException {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        if(!categoryOptional.isPresent())
            throw new CategoryNotFoundException(categoryId);

        return skillRepository.getAllByNameContainingAndCategoryId(keyword, categoryId, pageable);
    }


    @Override
    public Page<Skill> getAllByKeywordForAccountLogin(String keyword, Pageable pageable) {
        Account accountLogin = accountService.getAccountLogin();
        
        return skillRepository.getAllByNameContainingAndAccountId(keyword, accountLogin.getId(), pageable);
    }

    @Override
    public Skill create(Skill newSkill) throws SkillValidationException, SkillIsExistException, CategoryNotFoundException, LevelValidationException {

        skillValidation.validate(newSkill, GroupUpdateSkillWithLevel.class);

        Optional<Category> categoryOptional = categoryRepository.findById(newSkill.getCategoryId());

        if(!categoryOptional.isPresent())
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

        AccountHasSkill accountHasSkill = new AccountHasSkill();
        accountHasSkill.setAccountId(accountLogin.getId());
        accountHasSkill.setSkillId(newSkill.getId());
        accountHasSkill.setLevel(newSkill.getLevel());
        accountHasSkillRepository.save(accountHasSkill);

        newSkill.setCategory(categoryOptional.get());

        return newSkill;
    }

    @Override
    public Skill update(long skillId, Skill newSkill) throws SkillNotFoundException, SkillValidationException, SkillIsExistException {

        skillValidation.validate(newSkill, GroupUpdateSkillWithLevel.class);

        Account accountLogin = accountService.getAccountLogin();

        Skill oldSkill = skillRepository.getByAccountIdAndSkillIdAndCategoryId(accountLogin.getId(), skillId, newSkill.getCategoryId());

        if(oldSkill == null)
            throw new SkillNotFoundException(skillId);

        // have same skillName and categoryId
        if((oldSkill.getCategoryId() == newSkill.getCategoryId()) && (oldSkill.getName().equals(newSkill.getName()))) {
            AccountHasSkill oldAccountHasSkill = accountHasSkillRepository.getByAccountIdAndSkillId(accountLogin.getId(), skillId);
            oldAccountHasSkill.setLevel(newSkill.getLevel());
            oldAccountHasSkill.setUpdatedAt(new Date());
            accountHasSkillRepository.save(oldAccountHasSkill);

            oldSkill.setLevel(newSkill.getLevel());
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
            accountHasSkill.setLevel(newSkill.getLevel());
            accountHasSkill.setUpdatedAt(new Date());
            accountHasSkillRepository.save(accountHasSkill);

            skillTemp.setLevel(newSkill.getLevel());

            return skillTemp;
        } else {
            newSkill.setCreatedAt(new Date());
            newSkill.setUpdatedAt(new Date());
            skillTemp = skillRepository.save(newSkill);

            AccountHasSkill accountHasSkill = accountHasSkillRepository.getByAccountIdAndSkillId(accountLogin.getId(), skillId);
            accountHasSkill.setSkillId(newSkill.getId());
            accountHasSkill.setLevel(newSkill.getLevel());
            accountHasSkill.setUpdatedAt(new Date());
            accountHasSkillRepository.save(accountHasSkill);

            skillTemp.setLevel(newSkill.getLevel());

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

        return skillRepository.getAllByNameContainingAndCategoryId(name, categoryId, pageable);
    }

    @Override
    public Page<Skill> getAllByAccountId(long accountId, Pageable pageable) throws AccountNotFoundException {
        Optional<Account> accountOptional = accountRepository.findById(accountId);

        if(!accountOptional.isPresent())
            throw new AccountNotFoundException(accountId);

        return skillRepository.getAllByAccountId(accountId, pageable);
    }

    @Override
    public Page<Skill> search(List<Long> skillIds, Pageable pageable) {
        Page<Skill> skillPage;

        if (skillIds.isEmpty()) {
            skillPage = skillRepository.findAll(pageable);
            for (Skill skill : skillPage.getContent()) {
                skillIds.add(skill.getId());
            }
        }
        else
            skillPage = skillRepository.getAllByIdIsIn(skillIds, pageable);

        List<Skill> skills = IteratorUtils.toList(skillRepository.findAll().iterator());
        List<Category> categories = IteratorUtils.toList(categoryRepository.findAll().iterator());
        List<AccountHasSkill> accountHasSkills = IteratorUtils.toList(accountHasSkillRepository.findAll().iterator());
        List<Account> accounts = accountRepository.getAllBySkillIdIsIn(skillIds);

        for (Account account : accounts) {
            account.setSkills(new ArrayList<>());
            for (AccountHasSkill accountHasSkill : accountHasSkills) {
                if (accountHasSkill.getAccountId() == account.getId()) {
                    for (Skill skill : skills) {
                        if (skill.getId() == accountHasSkill.getSkillId()) {
                            account.getSkills().add(new Skill(skill.getId(), skill.getName(), accountHasSkill.getLevel(), skill.getCategoryId(), skill.getCreatedAt(), skill.getUpdatedAt()));
                            break;
                        }
                    }
                }
            }
        }

        for (Skill skill : skillPage.getContent()) {
            for (Category category : categories) {
                if (skill.getCategoryId() == category.getId()) {
                    skill.setCategory(category);
                    break;
                }
            }
            skill.setAccounts(new ArrayList<>());
            for (AccountHasSkill accountHasSkill : accountHasSkills) {
                if (accountHasSkill.getSkillId() == skill.getId()) {
                    for (Account account : accounts) {
                        if(account.getId() == accountHasSkill.getAccountId()) {
                            skill.getAccounts().add(account);
                            break;
                        }
                    }
                }
            }
        }

        return skillPage;
    }

    public static <T> List<T> copyIterator(Iterator<T> iter) {
        List<T> copy = new ArrayList<T>();
        while (iter.hasNext())
            copy.add(iter.next());
        return copy;
    }

}

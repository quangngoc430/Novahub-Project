package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.AccountHasSkill;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.repository.AccountHasSkillRepository;
import vn.novahub.helpdesk.repository.CategoryRepository;
import vn.novahub.helpdesk.repository.SkillRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Service
public class SkillServiceImpl implements SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private AccountHasSkillRepository accountHasSkillRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Skill createSkillByAccount(Skill skill, long accountId) {
        Skill oldSkill = skillRepository.findByName(skill.getName().toLowerCase());

        if(oldSkill == null) {
            skill.setName(skill.getName().toLowerCase());
            Skill newSkill = skillRepository.save(skill);
        } else {
            AccountHasSkill accountHasSkill = new AccountHasSkill();
            accountHasSkill.setAccountId(accountId);
            accountHasSkill.setSkillId(oldSkill.getId());
            accountHasSkillRepository.save(accountHasSkill);
        }

        return skill;
    }

    @Override
    public Skill createASkillOfACategory(Skill skill, long categoryId, HttpServletRequest request) {
        Skill oldSkill = skillRepository.findByName(skill.getName());

        if(oldSkill != null){
            return oldSkill;
        }

        skill.setName(skill.getName().toLowerCase());
        skill.setCategoryId(categoryId);

        return skillRepository.save(skill);
    }

    @Override
    public Skill updateSkill(Skill skill, long accountId, long skillId) {
        Skill oldSkill = skillRepository.findByName(skill.getName().toLowerCase());

        if(oldSkill == null){
            skill.setName(skill.getName().toLowerCase());
            Skill newSkill = skillRepository.save(skill);

            AccountHasSkill accountHasSkill = accountHasSkillRepository.findByAccountIdAndSkillId(accountId, skillId);
            accountHasSkill.setSkillId(newSkill.getId());
            accountHasSkillRepository.save(accountHasSkill);

            return newSkill;
        } else {
            AccountHasSkill accountHasSkill = accountHasSkillRepository.findByAccountIdAndSkillId(accountId, skillId);
            accountHasSkill.setSkillId(oldSkill.getId());
            accountHasSkillRepository.save(accountHasSkill);

            // delete old skill if is don't belong to any accounts
            skillRepository.deleteById(skillId);

            return oldSkill;
        }
    }

    @Override
    public Skill updateSkillByCategoryIdAndSkillId(Skill skill, long categoryId, long skillId, HttpServletRequest request) {
        if(categoryRepository.findById(categoryId).get() == null)
            return null;

        if(skillRepository.findById(skillId).get() == null)
            return null;

        skill.setId(skillId);
        skill.setCategoryId(categoryId);
        skill = skillRepository.save(skill);
        return skill;
    }

    @Override
    public Skill getASkillBySkillId(long skillId) {
        return skillRepository.findById(skillId).get();
    }

    @Override
    public Skill getASkillByCategoryIdAndSkillId(long categoryId, long skillId, HttpServletRequest request) {
        return skillRepository.findByIdAndCategoryId(skillId, categoryId);
    }

    @Override
    public void deteleASkillByCategoryIdAndSkillId(long categoryId, long skillId, HttpServletRequest request) {
        Category category = categoryRepository.findById(categoryId).get();

        if(category == null){
            // TODO: noti the categoryId isn't exist
        }

        Skill skill = skillRepository.findById(skillId).get();

        if(skill == null){
            // TODO: noti the skillId isn't exist
        }

        skillRepository.deleteById(skillId);
    }

    @Override
    public ArrayList<Skill> getAllSkillsOfACategory(long categoryId, String name, HttpServletRequest request) {
        Category category = categoryRepository.findById(categoryId).get();

        if(category == null)
            return null;

        return skillRepository.getAllByCategoryIdAndNameLike(categoryId, "%" + name + "%");
    }

    @Override
    public ArrayList<Skill> getAllSkillsOfAnAccount(String nameSkill, HttpServletRequest request) {
        nameSkill = "%" + nameSkill + "%";

        Account accountLogin = (Account) request.getSession().getAttribute("accountLogin");

        ArrayList<Skill> skillArrayList = skillRepository.getAllSkillsByAccountIdAndNameLike(accountLogin.getId(), nameSkill);

        return skillArrayList;
    }



}

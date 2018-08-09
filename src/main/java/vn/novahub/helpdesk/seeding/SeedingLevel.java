package vn.novahub.helpdesk.seeding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.AccountHasSkill;
import vn.novahub.helpdesk.model.Level;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.repository.AccountHasSkillRepository;
import vn.novahub.helpdesk.repository.LevelRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

@Component
public class LevelsSeeder {

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private AccountHasSkillRepository accountHasSkillRepository;

    public ArrayList<Level> generateData(ArrayList<Account> accountArrayList, ArrayList<Skill> skillArrayList) {
        ArrayList<Level> levelArrayList = new ArrayList<>();

        Random random = new Random();
        for(Account account : accountArrayList) {
            for(Skill skill : skillArrayList) {
                if(random.nextBoolean()) {
                    Level level = levelRepository.getByAccountIdAndSkillId(account.getId(), skill.getId());

                    if(level == null) {
                        level = new Level();
                        level.setValue(1 + random.nextInt(9));
                        level.setSkillId(skill.getId());
                        level.setAccountId(account.getId());
                        level.setCreatedAt(new Date());
                        level.setUpdatedAt(new Date());
                        level = levelRepository.save(level);

                        AccountHasSkill accountHasSkill = new AccountHasSkill();
                        accountHasSkill.setSkillId(skill.getId());
                        accountHasSkill.setAccountId(account.getId());
                        accountHasSkill.setCreatedAt(new Date());
                        accountHasSkill.setUpdatedAt(new Date());
                        accountHasSkill = accountHasSkillRepository.save(accountHasSkill);
                    }

                    levelArrayList.add(level);
                }
            }
        }

        return levelArrayList;
    }
}
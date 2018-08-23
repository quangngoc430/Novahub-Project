package vn.novahub.helpdesk.seeding;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.novahub.helpdesk.model.*;
import vn.novahub.helpdesk.repository.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ApplicationSeeder {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AccountHasSkillRepository accountHasSkillRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private DayOffTypeRepository dayOffTypeRepository;

    @Autowired
    private DayOffAccountRepository dayOffAccountRepository;

    @Autowired
    private DayOffRepository dayOffRepository;

    @Autowired
    private Seeder seeder;

    public void generateData() throws IOException {

        dayOffRepository.deleteAll();
        dayOffAccountRepository.deleteAll();
        dayOffTypeRepository.deleteAll();
        roleRepository.deleteAll();
        accountHasSkillRepository.deleteAll();
        categoryRepository.deleteAll();
        issueRepository.deleteAll();
        accountRepository.deleteAll();

        roleRepository.saveAll(
                seeder.generate("seeding/roles.json", new TypeReference<List<Role>>() {}));
        accountRepository.saveAll(
                seeder.generate("seeding/accounts.json", new TypeReference<List<Account>>() {}));
        categoryRepository.saveAll(
                seeder.generate("seeding/categories.json", new TypeReference<List<Category>>() {}));
        skillRepository.saveAll(
                seeder.generate("seeding/skills.json", new TypeReference<List<Skill>>() {}));
        accountHasSkillRepository.saveAll(
                seeder.generate("seeding/accountHasSkills.json", new TypeReference<List<AccountHasSkill>>() {}));
        issueRepository.saveAll(
                seeder.generate("seeding/issues.json", new TypeReference<List<Issue>>() {}));
//        dayOffTypeRepository.saveAll(
//                seeder.generate("seeding/day_off_type.json", new TypeReference<List<DayOffType>>() {}));
//        dayOffAccountRepository.saveAll(
//                seeder.generate("seeding/day_off_account.json", new TypeReference<List<DayOffAccount>>() {}));
//        dayOffRepository.saveAll(
//                seeder.generate("seeding/day_off.json", new TypeReference<List<DayOff>>() {}));
    }
}
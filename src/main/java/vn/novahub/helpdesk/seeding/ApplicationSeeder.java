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
    private RolesSeeder rolesSeeder;

    @Autowired
    private AccountsSeeder accountsSeeder;

    @Autowired
    private CategoriesSeeder categoriesSeeder;

    @Autowired
    private SkillsSeeder skillsSeeder;

    @Autowired
    private IssuesSeeder issuesSeeder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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

    public void generateData() throws IOException, ParseException, ClassNotFoundException {

        dayOffRepository.deleteAll();
        dayOffAccountRepository.deleteAll();
        dayOffTypeRepository.deleteAll();
        roleRepository.deleteAll();
        accountRepository.deleteAll();
        categoryRepository.deleteAll();
        issueRepository.deleteAll();

        ArrayList<Role> roleArrayList = rolesSeeder.generateData("seeding/roles.json");
        ArrayList<Account> accountArrayList = accountsSeeder.generateData("seeding/accounts.json");
        ArrayList<Category> categoryArrayList = categoriesSeeder.generateData("seeding/categories.json");
        ArrayList<Skill> skillArrayList = skillsSeeder.generateData("seeding/skills.json");
        ArrayList<Issue> issueArrayList = issuesSeeder.generateData("seeding/issues.json", accountArrayList);

        dayOffTypeRepository.saveAll(
                seeder.generate("seeding/day_off_type.json", new TypeReference<List<DayOffType>>() {}));
        dayOffAccountRepository.saveAll(
                seeder.generate("seeding/day_off_account.json", new TypeReference<List<DayOffAccount>>() {}));
        dayOffRepository.saveAll(
                seeder.generate("seeding/day_off.json", new TypeReference<List<DayOff>>() {}));
    }
}
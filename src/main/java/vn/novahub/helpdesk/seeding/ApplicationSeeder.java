package vn.novahub.helpdesk.seeding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.novahub.helpdesk.model.*;
import vn.novahub.helpdesk.repository.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

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
    private LevelsSeeder levelsSeeder;

    @Autowired
    private IssuesSeeder issuesSeeder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private IssueRepository issueRepository;

    public void generateData() throws IOException, ParseException {
        roleRepository.deleteAll();
        accountRepository.deleteAll();
        categoryRepository.deleteAll();
        levelRepository.deleteAll();
        issueRepository.deleteAll();

        ArrayList<Role> roleArrayList = rolesSeeder.generateData("seeding/roles.json");
        ArrayList<Account> accountArrayList = accountsSeeder.generateData("seeding/accounts.json");
        ArrayList<Category> categoryArrayList = categoriesSeeder.generateData("seeding/categories.json");
        ArrayList<Skill> skillArrayList = skillsSeeder.generateData("seeding/skills.json");
        ArrayList<Level> levelArrayList = levelsSeeder.generateData(accountArrayList, skillArrayList);
        ArrayList<Issue> issueArrayList = issuesSeeder.generateData("seeding/issues.json", accountArrayList);
    }
}
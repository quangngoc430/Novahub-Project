package vn.novahub.helpdesk.seeding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.novahub.helpdesk.model.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

@Component
public class ApplicationSeeder {

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

    public void generateData() throws IOException, ParseException {
        ArrayList<Account> accountArrayList = accountsSeeder.generateData("seeding/account-data.json");
        ArrayList<Category> categoryArrayList = categoriesSeeder.generateData("seeding/category-data.json");
        ArrayList<Skill> skillArrayList = skillsSeeder.generateData("seeding/skill-data.json");
        ArrayList<Level> levelArrayList = levelsSeeder.generateData(accountArrayList, skillArrayList);
        ArrayList<Issue> issueArrayList = issuesSeeder.generateData("seeding/issue-data.json", accountArrayList);
    }
}
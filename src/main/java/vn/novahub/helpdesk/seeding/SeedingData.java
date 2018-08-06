package vn.novahub.helpdesk.seeding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.novahub.helpdesk.model.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

@Component
public class SeedingData {

    @Autowired
    private AccountSeeding accountSeeding;

    @Autowired
    private CategorySeeding categorySeeding;

    @Autowired
    private SkillSeeding skillSeeding;

    @Autowired
    private LevelSeeding levelSeeding;

    @Autowired
    private IssueSeeding issueSeeding;

    public void generateData() throws IOException, ParseException {
        ArrayList<Account> accountArrayList = accountSeeding.generateData("data.json");
        ArrayList<Category> categoryArrayList = categorySeeding.generateData("data.json");
        ArrayList<Skill> skillArrayList = skillSeeding.generateData("data.json");
        ArrayList<Level> levelArrayList = levelSeeding.generateData(accountArrayList, skillArrayList);
        ArrayList<Issue> issueArrayList = issueSeeding.generateData("data.json", accountArrayList);
    }
}
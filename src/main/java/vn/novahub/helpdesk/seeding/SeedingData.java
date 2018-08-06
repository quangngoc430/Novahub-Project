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
    private SeedingAccount seedingAccount;

    @Autowired
    private SeedingCategory seedingCategory;

    @Autowired
    private SeedingSkill seedingSkill;

    @Autowired
    private SeedingLevel seedingLevel;

    @Autowired
    private SeedingIssue seedingIssue;

    public void generateData() throws IOException, ParseException {
        ArrayList<Account> accountArrayList = seedingAccount.generateData("data.json");
        ArrayList<Category> categoryArrayList = seedingCategory.generateData("data.json");
        ArrayList<Skill> skillArrayList = seedingSkill.generateData("data.json");
        ArrayList<Level> levelArrayList = seedingLevel.generateData(accountArrayList, skillArrayList);
        ArrayList<Issue> issueArrayList = seedingIssue.generateData("data.json", accountArrayList);
    }
}
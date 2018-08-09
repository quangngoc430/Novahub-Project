package vn.novahub.helpdesk.seeding;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import vn.novahub.helpdesk.enums.IssueEnum;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Issue;
import vn.novahub.helpdesk.repository.IssueRepository;
import vn.novahub.helpdesk.service.TokenService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

@Component
public class IssuesSeeder {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private TokenService tokenService;

    public ArrayList<Issue> generateData(String fileName, ArrayList<Account> accountArrayList) throws IOException {

        ArrayList<Issue> exampleIssueArrayList = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        Resource res = resourceLoader.getResource("classpath:" + fileName);
        JsonNode jsonNodeRoot = objectMapper.readValue(res.getFile(), JsonNode.class);
        JsonNode jsonNodeIssue = jsonNodeRoot.get("issue");

        for(int i = 0; i < jsonNodeIssue.size(); i++) {
            JsonNode currentJsonNode = jsonNodeIssue.get(i);

            Issue issue = new Issue();
            issue.setTitle(currentJsonNode.get("title").textValue());
            issue.setContent(currentJsonNode.get("content").textValue());
            exampleIssueArrayList.add(issue);
        }

        ArrayList<Issue> issueArrayList = new ArrayList<>();

        Random random = new Random();

        for(Account account: accountArrayList) {
            for(Issue issue: exampleIssueArrayList) {
                if(random.nextBoolean()) {
                    Issue newIssue = new Issue();
                    newIssue.setTitle(issue.getTitle());
                    newIssue.setContent(issue.getContent());
                    newIssue.setStatus(IssueEnum.PENDING.name());
                    newIssue.setToken(tokenService.generateToken(account.getId() + newIssue.getTitle() + (new Date()).getTime()));
                    newIssue.setAccountId(account.getId());
                    newIssue.setCreatedAt(new Date());
                    newIssue.setUpdatedAt(new Date());

                    newIssue = issueRepository.save(newIssue);

                    issueArrayList.add(newIssue);
                }
            }
        }

        return issueArrayList;
    }
}
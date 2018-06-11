package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.model.Issue;
import vn.novahub.helpdesk.repository.IssueRepository;

import java.util.Date;
import java.util.List;

@Service
public class IssueServiceImpl implements IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Override
    public Issue getIssueByIssueId(long issueId) {
        return issueRepository.findById(issueId).get();
    }

    @Override
    public Issue getIssueOfAccountByIssueIdAndAccountId(long issueId, long accountId){
        return issueRepository.getAnIssueByIssueIdAndAccountId(issueId, accountId);
    }

    @Override
    public Page<Issue> getAllIssuesByKeyword(String keyword, String status, Pageable pageable) {
        Page<Issue> issues;

        if(keyword == null)
            keyword = "";
        keyword = "%" + keyword + "%";

        if(status == null){
            issues = issueRepository.getAllIssuesByKeyWord(keyword, pageable);
        } else {
            issues = issueRepository.getAllIssuesByKeyWordAndStatus(keyword, status, pageable);
        }

        return issues;
    }

    @Override
    public Page<Issue> getAllIssuesOfAccountByKeyword(long accountId, String keyword, String status, Pageable pageable) {
        Page<Issue> issues;

        if(keyword == null)
            keyword = "";
        keyword = "%" + keyword + "%";

        if(status == null){
            issues = issueRepository.getAllIssuesByAccountIdAndKeyWord(accountId, keyword, pageable);
        } else {
            issues = issueRepository.getAllIssuesByAccountIdAndKeyWordAndStatus(accountId, keyword, status, pageable);
        }

        return issues;
    }

    @Override
    public Issue createIssue(Issue issue) {
        return issueRepository.save(issue);
    }

    @Override
    public Issue updateIssue(long issueId, Issue issue) {
        Issue oldIssue = issueRepository.findById(issueId).get();
        oldIssue.setUpdatedAt(new Date());

        if(!issue.getTitle().equals(oldIssue.getTitle()))
            oldIssue.setTitle(issue.getTitle());
        if(!issue.getContent().equals(oldIssue.getContent()))
            oldIssue.setContent(issue.getContent());
        if(!issue.getReplyMessage().equals(oldIssue.getReplyMessage()))
            oldIssue.setReplyMessage(issue.getReplyMessage());
        if(!issue.getStatus().equals(oldIssue.getStatus()))
            oldIssue.setStatus(issue.getStatus());
        if(!issue.getToken().equals(oldIssue.getToken()))
            oldIssue.setToken(issue.getToken());

        return issueRepository.save(issue);
    }

    @Override
    public boolean deleteIssue(long issueId) {

        boolean output = checkDeleteIssue(issueId);

        if(output == true)
            issueRepository.deleteById(issueId);

        return output;
    }

    boolean checkDeleteIssue(long issueId){
        return true;
    }

}

package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.novahub.helpdesk.constant.IssueConstant;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Issue;
import vn.novahub.helpdesk.repository.IssueRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class IssueServiceImpl implements IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AccountService accountService;

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
    public Issue createIssue(Issue issue, HttpServletRequest request) {
        Account accountLogin = accountService.getAccountLogin(request);

        issue.setCreatedAt(new Date());
        issue.setUpdatedAt(new Date());
        issue.setToken(tokenService.generateToken(accountLogin.getId() + issue.getTitle()));
        issue.setAccountId(accountLogin.getId());

        return issueRepository.save(issue);
    }

    @Override
    @Transactional
    public Issue updateIssue(long issueId, Issue issue, HttpServletRequest request) {
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

        return issueRepository.save(oldIssue);
    }

    @Override
    public void deleteIssue(long issueId) {

        issueRepository.deleteById(issueId);
    }

    @Override
    public boolean approveIssue(long issueId, String token) {
        Issue issue = issueRepository.findByIdAndToken(issueId, token);

        if(issue == null){
            return false;
        }

        if(issue.getToken() != null) {
            issue.setToken(null);
            issue.setStatus(IssueConstant.STATUS_APPROVE);
            issueRepository.save(issue);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean denyIssue(long issueId, String token) {
        Issue issue = issueRepository.findByIdAndToken(issueId, token);

        if(issue == null){
            return false;
        }

        if(issue.getToken() != null) {
            issue.setToken(null);
            issue.setStatus(IssueConstant.STATUS_DENY);
            issueRepository.save(issue);
            return true;
        } else {
            return false;
        }
    }

}

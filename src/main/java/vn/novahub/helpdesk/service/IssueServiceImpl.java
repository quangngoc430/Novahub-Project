package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.novahub.helpdesk.constant.IssueConstant;
import vn.novahub.helpdesk.exception.IssueNotFoundException;
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
    public Issue getByIssueId(long issueId) throws IssueNotFoundException {
        Issue issue = issueRepository.findById(issueId).get();

        if(issue == null)
            throw new IssueNotFoundException(issueId);

        return issue;
    }

    @Override
    public Issue getOfAccountByIssueIdAndAccountId(long issueId, HttpServletRequest request) throws IssueNotFoundException {
        Account accountLogin = accountService.getAccountLogin(request);

        Issue issue = issueRepository.getAnIssueByIssueIdAndAccountId(issueId, accountLogin.getId());

        if(issue == null)
            throw new IssueNotFoundException(issueId);

        return issue;
    }

    @Override
    public Page<Issue> getAllByKeyword(String keyword, String status, Pageable pageable) {
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
    public Page<Issue> getAllOfAccountByKeyword(String keyword, String status, Pageable pageable, HttpServletRequest request) {

        Account accountLogin = accountService.getAccountLogin(request);

        if(keyword == null)
            keyword = "";
        keyword = "%" + keyword + "%";

        Page<Issue> issues;

        if(status == null){
            issues = issueRepository.getAllIssuesByAccountIdAndKeyWord(accountLogin.getId(), keyword, pageable);
        } else {
            issues = issueRepository.getAllIssuesByAccountIdAndKeyWordAndStatus(accountLogin.getId(), keyword, status, pageable);
        }

        return issues;
    }

    @Override
    public Issue create(Issue issue, HttpServletRequest request) {
        Account accountLogin = accountService.getAccountLogin(request);

        issue.setCreatedAt(new Date());
        issue.setUpdatedAt(new Date());
        issue.setToken(tokenService.generateToken(accountLogin.getId() + issue.getTitle()));
        issue.setAccountId(accountLogin.getId());

        return issueRepository.save(issue);
    }

    @Override
    @Transactional
    public Issue update(long issueId, Issue issue) throws IssueNotFoundException {
        Issue oldIssue = issueRepository.findById(issueId).get();

        if(oldIssue == null)
            throw new IssueNotFoundException(issueId);

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
    public void delete(long issueId) throws IssueNotFoundException {

        if(!issueRepository.existsById(issueId))
            throw new IssueNotFoundException(issueId);

        issueRepository.deleteById(issueId);
    }

    @Override
    public boolean approve(long issueId, String token) throws IssueNotFoundException {
        Issue issue = issueRepository.findByIdAndToken(issueId, token);

        if(issue == null)
            throw new IssueNotFoundException(issueId);

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
    public boolean deny(long issueId, String token) throws IssueNotFoundException {
        Issue issue = issueRepository.findByIdAndToken(issueId, token);

        if(issue == null)
            throw new IssueNotFoundException(issueId);

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

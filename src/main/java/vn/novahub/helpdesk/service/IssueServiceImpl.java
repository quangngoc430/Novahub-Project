package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.constant.IssueConstant;
import vn.novahub.helpdesk.exception.IssueIsClosedException;
import vn.novahub.helpdesk.exception.IssueNotFoundException;
import vn.novahub.helpdesk.exception.IssueValidationException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Issue;
import vn.novahub.helpdesk.repository.IssueRepository;
import vn.novahub.helpdesk.validation.IssueValidation;

import javax.validation.groups.Default;
import java.util.Date;

@Service
public class IssueServiceImpl implements IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private IssueValidation issueValidation;

    @Override
    public boolean isIssueOfAccountLogin(long issueId) {
        Account account = accountService.getAccountLogin();

        return issueRepository.existsByIdAndAccountId(issueId, account.getId());

    }

    @Override
    public Issue getByIssueId(long issueId) throws IssueNotFoundException {
        Issue issue = issueRepository.getById(issueId);

        if(issue == null)
            throw new IssueNotFoundException(issueId);

        return issue;
    }

    @Override
    public Issue getOfAccountLoginByIssueId(long issueId) throws IssueNotFoundException {
        Account accountLogin = accountService.getAccountLogin();

        Issue issue = issueRepository.getByIdAndAccountId(issueId, accountLogin.getId());

        if(issue == null)
            throw new IssueNotFoundException(issueId, accountLogin.getId());

        return issue;
    }

    @Override
    public Page<Issue> getAllByKeyword(String keyword, String status, Pageable pageable) {
        if(status.equals("")){
            return issueRepository.getAllByTitleLikeOrContentLike("%" + keyword + "%", "%" + keyword + "%", pageable);
        }

        return issueRepository.getAllByTitleLikeAndContentLikeAndStatus("%" + keyword + "%", "%" + keyword + "%", status, pageable);
    }

    @Override
    public Page<Issue> getAllOfAccountLoginByKeyword(String keyword, String status, Pageable pageable) {

        Account accountLogin = accountService.getAccountLogin();

        if(status.equals("")){
            return issueRepository.getAllByAccountIdAndKeyWord(accountLogin.getId(), "%" + keyword + "%", pageable);
        }

        return issueRepository.getAllByAccountIdAndKeyWordAndStatus(accountLogin.getId(), "%" + keyword + "%", status, pageable);
    }

    @Override
    public Issue create(Issue issue) throws IssueValidationException {
        Account accountLogin = accountService.getAccountLogin();

        issue.setCreatedAt(new Date());
        issue.setUpdatedAt(new Date());
        issue.setToken(tokenService.generateToken(accountLogin.getId() + issue.getTitle()));
        issue.setAccountId(accountLogin.getId());

        issueValidation.validate(issue, Default.class);

        return issueRepository.save(issue);
    }

    @Override
    public Issue update(long issueId, Issue issue) throws IssueNotFoundException, IssueValidationException {
        Account account = accountService.getAccountLogin();

        Issue oldIssue = issueRepository.getByIdAndAccountId(issueId, account.getId());

        if(oldIssue == null)
            throw new IssueNotFoundException(issueId, account.getId());

        oldIssue.setUpdatedAt(new Date());

        if(issue.getTitle() != null)
            oldIssue.setTitle(issue.getTitle());
        if(issue.getContent() != null)
            oldIssue.setContent(issue.getContent());
        if(issue.getReplyMessage() != null)
            oldIssue.setReplyMessage(issue.getReplyMessage());

        issueValidation.validate(oldIssue, Default.class);

        return issueRepository.save(oldIssue);
    }

    @Override
    public Issue updateForAdmin(long issueId, Issue issue) throws IssueNotFoundException, IssueValidationException {
        Issue oldIssue = issueRepository.getById(issueId);

        if(oldIssue == null)
            throw new IssueNotFoundException(issueId);

        oldIssue.setUpdatedAt(new Date());

        if(issue.getTitle() != null)
            oldIssue.setTitle(issue.getTitle());
        if(issue.getContent() != null)
            oldIssue.setContent(issue.getContent());
        if(issue.getReplyMessage() != null)
            oldIssue.setReplyMessage(issue.getReplyMessage());
        if(issue.getStatus() != null)
            oldIssue.setStatus(issue.getStatus());

        issueValidation.validate(oldIssue, Default.class);

        return issueRepository.save(oldIssue);
    }

    @Override
    public void delete(long issueId) throws IssueNotFoundException {
        Account accountLogin = accountService.getAccountLogin();

        if(!issueRepository.existsByIdAndAccountId(issueId, accountLogin.getId()))
            throw new IssueNotFoundException(issueId, accountLogin.getId());

        issueRepository.deleteByIdAndAccountId(issueId, accountLogin.getId());
    }

    @Override
    public void deleteForAdmin(long issueId) throws IssueNotFoundException {
        if(!issueRepository.existsById(issueId))
            throw new IssueNotFoundException(issueId);

        issueRepository.deleteById(issueId);
    }

    @Override
    public void approve(long issueId, String token) throws IssueNotFoundException, IssueIsClosedException {
        Issue issue = issueRepository.findByIdAndToken(issueId, token);

        if(issue == null)
            throw new IssueNotFoundException(issueId);

        if(issue.getToken() == null)
            throw new IssueIsClosedException(issueId);

        issue.setToken(null);
        issue.setStatus(IssueConstant.STATUS_APPROVE);
        issueRepository.save(issue);
    }

    @Override
    public void deny(long issueId, String token) throws IssueNotFoundException, IssueIsClosedException {
        Issue issue = issueRepository.findByIdAndToken(issueId, token);

        if(issue == null)
            throw new IssueNotFoundException(issueId);

        if(issue.getToken() == null)
            throw new IssueIsClosedException(issueId);

        issue.setToken(null);
        issue.setStatus(IssueConstant.STATUS_DENY);
        issueRepository.save(issue);

    }

}

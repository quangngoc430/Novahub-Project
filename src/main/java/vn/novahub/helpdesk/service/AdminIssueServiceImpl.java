package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.constant.IssueConstant;
import vn.novahub.helpdesk.exception.IssueIsClosedException;
import vn.novahub.helpdesk.exception.IssueNotFoundException;
import vn.novahub.helpdesk.exception.IssueValidationException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Issue;
import vn.novahub.helpdesk.model.Mail;
import vn.novahub.helpdesk.repository.AccountRepository;
import vn.novahub.helpdesk.repository.IssueRepository;
import vn.novahub.helpdesk.validation.GroupUpdateIssue;
import vn.novahub.helpdesk.validation.IssueValidation;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;

@Service
@PropertySource("classpath:email.properties")
public class AdminIssueServiceImpl implements AdminIssueService{

    @Autowired
    private Environment env;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueValidation issueValidation;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MailService mailService;

    @Override
    public Issue findOne(long issueId) throws IssueNotFoundException {
        Issue issue = issueRepository.getById(issueId);

        if (issue == null)
            throw new IssueNotFoundException(issueId);

        return issue;
    }

    @Override
    public Page<Issue> getAllByKeywordAndStatus(String keyword, String status, Pageable pageable) {
        if (status.equals(""))
            return issueRepository.getAllByTitleLikeOrContentLike("%" + keyword + "%", pageable);

        return issueRepository.getAllByTitleLikeOrContentLikeAndStatus("%" + keyword + "%", status, pageable);
    }

    @Override
    public Issue update(long issueId, Issue issue) throws IssueNotFoundException, IssueValidationException, MessagingException, IOException {
        issueValidation.validate(issue, GroupUpdateIssue.class);

        boolean isSendMailUpdateIssue = false;

        Issue oldIssue = issueRepository.getById(issueId);

        if (oldIssue == null)
            throw new IssueNotFoundException(issueId);

        oldIssue.setUpdatedAt(new Date());

        oldIssue.setTitle(issue.getTitle());
        oldIssue.setContent(issue.getContent());
        if (!issue.getReplyMessage().equals(""))
            oldIssue.setReplyMessage(issue.getReplyMessage());
        if (!issue.getStatus().equals("")) {
            if (oldIssue.getStatus().equals(IssueConstant.STATUS_PENDING) &&
                    issue.getStatus().equals(IssueConstant.STATUS_DENY)) {
                oldIssue.setToken(null);
                isSendMailUpdateIssue = true;
            }

            if (oldIssue.getStatus().equals(IssueConstant.STATUS_PENDING) &&
                    issue.getStatus().equals(IssueConstant.STATUS_APPROVE)) {
                oldIssue.setToken(null);
                isSendMailUpdateIssue = true;
            }

            oldIssue.setStatus(issue.getStatus());
        }

        oldIssue = issueRepository.save(oldIssue);

        if(isSendMailUpdateIssue)
            sendMailUpdateIssueForUser(oldIssue);

        return oldIssue;
    }

    public void delete(long issueId) throws IssueNotFoundException{
        if (!issueRepository.existsById(issueId))
            throw new IssueNotFoundException(issueId);

        issueRepository.deleteById(issueId);
    }

    @Override
    public void approve(long issueId, String token) throws IssueNotFoundException, IssueIsClosedException, MessagingException, IOException {
        Issue issue = issueRepository.findByIdAndToken(issueId, token);

        if (issue == null)
            throw new IssueNotFoundException(issueId);

        if (issue.getToken() == null)
            throw new IssueIsClosedException(issueId);

        issue.setToken(null);
        issue.setStatus(IssueConstant.STATUS_APPROVE);
        issue = issueRepository.save(issue);

        sendMailUpdateIssueForUser(issue);
    }

    @Override
    public void deny(long issueId, String token) throws IssueNotFoundException, IssueIsClosedException, MessagingException, IOException {
        Issue issue = issueRepository.findByIdAndToken(issueId, token);

        if (issue == null)
            throw new IssueNotFoundException(issueId);

        if (issue.getToken() == null)
            throw new IssueIsClosedException(issueId);

        issue.setToken(null);
        issue.setStatus(IssueConstant.STATUS_DENY);
        issue = issueRepository.save(issue);

        sendMailUpdateIssueForUser(issue);
    }

    private void sendMailUpdateIssueForUser(Issue issue) throws MessagingException, IOException {
        Account account = accountRepository.getById(issue.getAccountId());

        Mail mail = new Mail();
        String subject = env.getProperty("subject_email_update_issue_account");
        subject = subject.replace("{issue-id}", String.valueOf(issue.getId()));
        mail.setSubject(subject);
        String content = mailService.getContentMail("update_issue_account.html");
        content = content.replace("{issue-id}", String.valueOf(issue.getId()));
        content = content.replace("{email}", account.getEmail());
        content = content.replace("{title}", issue.getTitle());
        content = content.replace("{content}", issue.getContent());
        content = content.replace("{status}", issue.getStatus());
        content = content.replace("{reply-message}", (issue.getReplyMessage() == null) ? "NONE" : issue.getReplyMessage());
        mail.setContent(content);
        mail.setEmailReceiving(new String[]{account.getEmail()});

        mailService.sendHTMLMail(mail);
    }
}

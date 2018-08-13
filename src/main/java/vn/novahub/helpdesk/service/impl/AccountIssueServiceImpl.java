package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.enums.IssueEnum;
import vn.novahub.helpdesk.enums.RoleEnum;
import vn.novahub.helpdesk.exception.AccountNotFoundException;
import vn.novahub.helpdesk.exception.IssueIsClosedException;
import vn.novahub.helpdesk.exception.IssueNotFoundException;
import vn.novahub.helpdesk.exception.IssueValidationException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Issue;
import vn.novahub.helpdesk.model.Mail;
import vn.novahub.helpdesk.repository.AccountRepository;
import vn.novahub.helpdesk.repository.IssueRepository;
import vn.novahub.helpdesk.service.AccountIssueService;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.MailService;
import vn.novahub.helpdesk.service.TokenService;
import vn.novahub.helpdesk.validation.GroupCreateIssue;
import vn.novahub.helpdesk.validation.IssueValidation;

import javax.mail.MessagingException;
import javax.validation.groups.Default;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
@PropertySource("classpath:email.properties")
public class AccountIssueServiceImpl implements AccountIssueService {

    @Value("${host_url}")
    private String hostUrl;

    @Autowired
    private Environment env;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IssueValidation issueValidation;

    @Autowired
    private MailService mailService;

    @Override
    public Page<Issue> getAllByKeywordAndStatus(String keyword, String status, Pageable pageable) {
        Account accountLogin = accountService.getAccountLogin();

        if (status.equals(""))
            return issueRepository.getAllByAccountIdAndTitleContainingOrContentContaining(accountLogin.getId(), keyword, pageable);

        return issueRepository.getAllByAccountIdAndStatusAndTitleContainingAndContentContaining(accountLogin.getId(), status, keyword, pageable);
    }

    @Override
    public Issue findOne(long issueId) throws IssueNotFoundException {
        Account accountLogin = accountService.getAccountLogin();

        Issue issue = issueRepository.getByIdAndAccountIdAndStatusIsNot(issueId, accountLogin.getId(), IssueEnum.CANCELLED.name());

        if (issue == null)
            throw new IssueNotFoundException(issueId, accountLogin.getId());

        return issue;
    }

    @Override
    public Issue create(Issue issue) throws IssueValidationException, MessagingException, IOException, AccountNotFoundException {

        issueValidation.validate(issue, GroupCreateIssue.class);

        Account accountLogin = accountService.getAccountLogin();

        issue.setCreatedAt(new Date());
        issue.setUpdatedAt(new Date());
        issue.setStatus(IssueEnum.PENDING.name());
        issue.setToken(tokenService.generateToken(accountLogin.getId() + issue.getTitle() + (new Date()).getTime()));
        issue.setAccountId(accountLogin.getId());
        issue = issueRepository.save(issue);

        Optional<Account> accountOptional = accountRepository.findById(issue.getAccountId());

        if(!accountOptional.isPresent())
            throw new AccountNotFoundException(issue.getAccountId());

        issue.setAccount(accountLogin);

        sendMailCreateIssueForAdmin(issue, accountLogin);

        return issue;
    }

    @Override
    public Issue update(long issueId, Issue issue) throws IssueNotFoundException, IssueValidationException, MessagingException, IOException, IssueIsClosedException, AccountNotFoundException {

        Account account = accountService.getAccountLogin();

        Issue oldIssue = issueRepository.getByIdAndAccountId(issueId, account.getId());

        if (oldIssue == null)
            throw new IssueNotFoundException(issueId, account.getId());

        if (oldIssue.getToken() == null)
            throw new IssueIsClosedException(issueId);

        boolean isSendMail = false;

        if(issue.getTitle() != null) {
            oldIssue.setTitle(issue.getTitle());
            isSendMail = true;
        }
        if(issue.getContent() != null) {
            oldIssue.setContent(issue.getContent());
            isSendMail = true;
        }

        issueValidation.validate(oldIssue, Default.class);

        if(isSendMail) {
            oldIssue.setUpdatedAt(new Date());
            oldIssue = issueRepository.save(oldIssue);
            sendMailUpdateIssueForAdmin(oldIssue);
        }

        return oldIssue;
    }

    @Override
    public void delete(long issueId) throws IssueNotFoundException {
        Account accountLogin = accountService.getAccountLogin();

        Issue issue = issueRepository.getByIdAndAccountIdAndStatusIsNot(issueId, accountLogin.getId(), IssueEnum.CANCELLED.name());

        if (issue == null)
            throw new IssueNotFoundException(issueId, accountLogin.getId());

        issue.setStatus(IssueEnum.CANCELLED.name());

        issueRepository.save(issue);
    }

    private void sendMailCreateIssueForAdmin(Issue issue, Account accountLogin) throws MessagingException, IOException {
        Mail mail = new Mail();
        String subject = env.getProperty("subject_email_create_issue");
        subject = subject.replace("{issue-id}", String.valueOf(issue.getId()));
        mail.setSubject(subject);
        String content = mailService.getContentMail("create_issue.html");
        content = content.replace("{issue-id}", String.valueOf(issue.getId()));
        content = content.replace("{email}", accountLogin.getEmail());
        content = content.replace("{title}", issue.getTitle());
        content = content.replace("{content}", issue.getContent());
        content = content.replace("{status}", issue.getStatus());
        content = content.replace("{reply-message}", (issue.getReplyMessage() == null) ? IssueEnum.NONE.name() : issue.getReplyMessage());
        content = content.replace("{url-approve-issue}", hostUrl + "/api/issues/" + issue.getId() + "/action?status=APPROVE&token=" + issue.getToken());
        content = content.replace("{url-deny-issue}", hostUrl + "/api/issues/" + issue.getId() + "/action?status=DENY&token=" + issue.getToken());
        mail.setContent(content);

        mail.setEmailReceiving(mailService.getEmailsOfAdminAndClerk().toArray(new String[0]));

        mailService.sendHTMLMail(mail);
    }

    private void sendMailUpdateIssueForAdmin(Issue issue) throws MessagingException, IOException, AccountNotFoundException {
        Optional<Account> accountOptional = accountRepository.findById(issue.getAccountId());

        if(!accountOptional.isPresent())
            throw new AccountNotFoundException(issue.getAccountId());

        Account accountLogin = accountOptional.get();

        Mail mail = new Mail();
        String subject = env.getProperty("subject_email_update_issue_admin");
        subject = subject.replace("{issue-id}", String.valueOf(issue.getId()));
        mail.setSubject(subject);
        String content = mailService.getContentMail("update_issue_admin.html");
        content = content.replace("{issue-id}", String.valueOf(issue.getId()));
        content = content.replace("{email}", accountLogin.getEmail());
        content = content.replace("{title}", issue.getTitle());
        content = content.replace("{content}", issue.getContent());
        content = content.replace("{status}", issue.getStatus());
        content = content.replace("{reply-message}", (issue.getReplyMessage() == null) ? IssueEnum.NONE.name() : issue.getReplyMessage());
        mail.setContent(content);

        mail.setEmailReceiving(mailService.getEmailsOfAdminAndClerk().toArray(new String[0]));

        mailService.sendHTMLMail(mail);
    }

}


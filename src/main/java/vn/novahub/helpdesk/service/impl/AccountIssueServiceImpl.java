package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.enums.IssueEnum;
import vn.novahub.helpdesk.enums.RoleEnum;
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

@Service
@PropertySource("classpath:email.properties")
public class AccountIssueServiceImpl implements AccountIssueService {

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

        Issue issue = issueRepository.getByIdAndAccountId(issueId, accountLogin.getId());

        if (issue == null)
            throw new IssueNotFoundException(issueId, accountLogin.getId());

        return issue;
    }

    @Override
    public Issue create(Issue issue) throws IssueValidationException, MessagingException, IOException {

        issueValidation.validate(issue, GroupCreateIssue.class);

        Account accountLogin = accountService.getAccountLogin();

        issue.setCreatedAt(new Date());
        issue.setUpdatedAt(new Date());
        issue.setStatus(IssueEnum.PENDING.name());
        issue.setToken(tokenService.generateToken(accountLogin.getId() + issue.getTitle()));
        issue.setAccountId(accountLogin.getId());

        issue = issueRepository.save(issue);

        sendMailCreateIssueForAdmin(issue, accountLogin);

        return issue;
    }

    @Override
    public Issue update(long issueId, Issue issue) throws IssueNotFoundException, IssueValidationException, MessagingException, IOException {

        Account account = accountService.getAccountLogin();

        Issue oldIssue = issueRepository.getByIdAndAccountId(issueId, account.getId());

        if (oldIssue == null)
            throw new IssueNotFoundException(issueId, account.getId());

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

        if (!issueRepository.existsByIdAndAccountId(issueId, accountLogin.getId()))
            throw new IssueNotFoundException(issueId, accountLogin.getId());

        issueRepository.deleteByIdAndAccountId(issueId, accountLogin.getId());
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
        content = content.replace("{url-approve-issue}", "http://localhost:8080/api/issues/" + issue.getId() + "/action?status=APPROVE&token=" + issue.getToken());
        content = content.replace("{url-deny-issue}", "http://localhost:8080/api/issues/" + issue.getId() + "/action?status=DENY&token=" + issue.getToken());
        mail.setContent(content);

        mail.setEmailReceiving(getEmailsOfAdminAndClerk().toArray(new String[0]));

        mailService.sendHTMLMail(mail);
    }

    private void sendMailUpdateIssueForAdmin(Issue issue) throws MessagingException, IOException {
        Account accountLogin = accountRepository.getById(issue.getAccountId());

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

        mail.setEmailReceiving(getEmailsOfAdminAndClerk().toArray(new String[0]));

        mailService.sendHTMLMail(mail);
    }

    private ArrayList<String> getEmailsOfAdminAndClerk(){
        ArrayList<Account> adminList = (ArrayList<Account>) (accountRepository.getAllByRoleName(RoleEnum.ADMIN.name()));
        ArrayList<Account> clerkList = (ArrayList<Account>) (accountRepository.getAllByRoleName(RoleEnum.CLERK.name()));

        ArrayList<String> emails = new ArrayList<>();

        if(adminList != null)
            for (Account account : adminList)
                emails.add(account.getEmail());

        if(clerkList != null)
            for (Account account : clerkList)
                emails.add(account.getEmail());

        return emails;
    }
}

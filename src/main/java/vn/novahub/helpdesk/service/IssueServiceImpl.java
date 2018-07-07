package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.constant.IssueConstant;
import vn.novahub.helpdesk.constant.RoleConstant;
import vn.novahub.helpdesk.exception.IssueIsClosedException;
import vn.novahub.helpdesk.exception.IssueNotFoundException;
import vn.novahub.helpdesk.exception.IssueValidationException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Issue;
import vn.novahub.helpdesk.model.Mail;
import vn.novahub.helpdesk.repository.AccountRepository;
import vn.novahub.helpdesk.repository.IssueRepository;
import vn.novahub.helpdesk.repository.RoleRepository;
import vn.novahub.helpdesk.thread.MailThread;
import vn.novahub.helpdesk.validation.IssueValidation;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@PropertySource("classpath:email.properties")
public class IssueServiceImpl implements IssueService {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Environment env;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private IssueValidation issueValidation;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public boolean isIssueOfAccountLogin(long issueId) {
        Account account = accountService.getAccountLogin();

        return issueRepository.existsByIdAndAccountId(issueId, account.getId());
    }

    @Override
    public Page<Issue> getAllByKeywordAndStatusForAdmin(String keyword, String status, Pageable pageable) {
        if (status.equals(""))
            return issueRepository.getAllByTitleLikeOrContentLike("%" + keyword + "%", pageable);

        return issueRepository.getAllByTitleLikeOrContentLikeAndStatus("%" + keyword + "%", status, pageable);
    }

    @Override
    public Issue getByIdForAdmin(long issueId) throws IssueNotFoundException {
        Issue issue = issueRepository.getById(issueId);

        if (issue == null)
            throw new IssueNotFoundException(issueId);

        return issue;
    }

    @Override
    public Issue updateForAdmin(long issueId, Issue issue) throws IssueNotFoundException, IssueValidationException {
        Issue oldIssue = issueRepository.getById(issueId);

        if (oldIssue == null)
            throw new IssueNotFoundException(issueId);

        oldIssue.setUpdatedAt(new Date());

        if (issue.getTitle() != null)
            oldIssue.setTitle(issue.getTitle());
        if (issue.getContent() != null)
            oldIssue.setContent(issue.getContent());
        if (issue.getReplyMessage() != null)
            oldIssue.setReplyMessage(issue.getReplyMessage());
        if (issue.getStatus() != null) {
            if (oldIssue.getStatus().equals(IssueConstant.STATUS_PENDING) &&
                    issue.getStatus().equals(IssueConstant.STATUS_DENY)) {
                //TODO: send mail deny
                oldIssue.setToken(null);
            }

            if (oldIssue.getStatus().equals(IssueConstant.STATUS_PENDING) &&
                    issue.getStatus().equals(IssueConstant.STATUS_APPROVE)) {
                //TODO: send mail approve
                oldIssue.setToken(null);
            }

            oldIssue.setStatus(issue.getStatus());
        }

        issueValidation.validate(oldIssue, Default.class);

        return issueRepository.save(oldIssue);
    }

    @Override
    public void deleteForAdmin(long issueId) throws IssueNotFoundException {
        if (!issueRepository.existsById(issueId))
            throw new IssueNotFoundException(issueId);

        issueRepository.deleteById(issueId);
    }

    @Override
    public Page<Issue> getAllByKeywordAndStatus(String keyword, String status, Pageable pageable) {

        Account accountLogin = accountService.getAccountLogin();

        if (status.equals(""))
            return issueRepository.getAllByAccountIdAndContentLikeOrTitleLike(accountLogin.getId(), "%" + keyword + "%", pageable);

        return issueRepository.getAllByAccountIdAndTitleLikeOrContentLikeAndStatus(accountLogin.getId(), "%" + keyword + "%", status, pageable);
    }

    @Override
    public Issue getById(long issueId) throws IssueNotFoundException {
        Account accountLogin = accountService.getAccountLogin();

        Issue issue = issueRepository.getByIdAndAccountId(issueId, accountLogin.getId());

        if (issue == null)
            throw new IssueNotFoundException(issueId, accountLogin.getId());

        return issue;
    }

    @Override
    public Issue create(Issue issue) throws IssueValidationException, MessagingException {
        Account accountLogin = accountService.getAccountLogin();

        issue.setCreatedAt(new Date());
        issue.setUpdatedAt(new Date());
        issue.setStatus(IssueConstant.STATUS_PENDING);
        issue.setToken(tokenService.generateToken(accountLogin.getId() + issue.getTitle()));
        issue.setAccountId(accountLogin.getId());

        issueValidation.validate(issue, Default.class);

        issue = issueRepository.save(issue);

        // TODO: send email
        sendMailCreateIssue(issue, accountLogin);

        return issue;
    }

    @Autowired
    private JavaMailSender mailSender;

    private void sendMailCreateIssue(Issue issue, Account accountLogin) throws MessagingException {
        Mail mail = new Mail();
        String subject = env.getProperty("subject_email_create_issue");
        subject = subject.replace("{issue-id}", String.valueOf(issue.getId()));
        mail.setSubject(subject);
        String content = env.getProperty("content_email_create_issue");
        content = content.replace("{issue-id}", String.valueOf(issue.getId()));
        content = content.replace("{email}", accountLogin.getEmail());
        content = content.replace("{title}", issue.getTitle());
        content = content.replace("{content}", issue.getContent());
        content = content.replace("{status}", issue.getStatus());
        content = content.replace("{reply-message}", (issue.getReplyMessage() == null) ? "NONE" : issue.getReplyMessage());
        mail.setContent(content);

        ArrayList<Account> adminList = (ArrayList<Account>) (accountRepository.getAllByRoleName(RoleConstant.ROLE_ADMIN));
        ArrayList<Account> clerkList = (ArrayList<Account>) (accountRepository.getAllByRoleName(RoleConstant.ROLE_CLERK));

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");

        ArrayList<String> emails = new ArrayList<>();
        emails.add("helpdesk@novahub.vn");
        emails.add("ngocbui@novahub.vn");
        emails.add("builamquangngoc91@gmail.com");
        emails.add("ngoc.bui150019@vnuk.edu.vn");


        helper.setTo(emails.toArray(new String[0]));
        helper.setSubject(mail.getSubject());
        message.setContent(mail.getContent(), "text/html");

        mailSender.send(message);
//        if(clerkList != null)
//        for(Account clerkAccount : clerkList){
//            mail.setEmailReceiving(clerkAccount.getEmail());
//            MailThread mailThread = new MailThread();
//            applicationContext.getAutowireCapableBeanFactory().autowireBean(mailThread);
//            mailThread.setMail(mail);
//            mailThread.run();
//        }
//
//        if(adminList != null)
//        for(Account adminAccount : adminList){
//            mail.setEmailReceiving(adminAccount.getEmail());
//            MailThread mailThread = new MailThread();
//            applicationContext.getAutowireCapableBeanFactory().autowireBean(mailThread);
//            mailThread.setMail(mail);
//            mailThread.run();
//        }
    }

    @Override
    public Issue update(long issueId, Issue issue) throws IssueNotFoundException, IssueValidationException {
        Account account = accountService.getAccountLogin();

        Issue oldIssue = issueRepository.getByIdAndAccountId(issueId, account.getId());

        if (oldIssue == null)
            throw new IssueNotFoundException(issueId, account.getId());

        oldIssue.setUpdatedAt(new Date());

        if (issue.getTitle() != null)
            oldIssue.setTitle(issue.getTitle());
        if (issue.getContent() != null)
            oldIssue.setContent(issue.getContent());
        if (issue.getReplyMessage() != null)
            oldIssue.setReplyMessage(issue.getReplyMessage());

        issueValidation.validate(oldIssue, Default.class);

        return issueRepository.save(oldIssue);
    }

    @Override
    public void delete(long issueId) throws IssueNotFoundException {
        Account accountLogin = accountService.getAccountLogin();

        if (!issueRepository.existsByIdAndAccountId(issueId, accountLogin.getId()))
            throw new IssueNotFoundException(issueId, accountLogin.getId());

        issueRepository.deleteByIdAndAccountId(issueId, accountLogin.getId());
    }

    @Override
    public void approve(long issueId, String token) throws IssueNotFoundException, IssueIsClosedException {
        Issue issue = issueRepository.findByIdAndToken(issueId, token);

        if (issue == null)
            throw new IssueNotFoundException(issueId);

        if (issue.getToken() == null)
            throw new IssueIsClosedException(issueId);

        issue.setToken(null);
        issue.setStatus(IssueConstant.STATUS_APPROVE);
        issueRepository.save(issue);

        // TODO: send email approve
    }

    @Override
    public void deny(long issueId, String token) throws IssueNotFoundException, IssueIsClosedException {
        Issue issue = issueRepository.findByIdAndToken(issueId, token);

        if (issue == null)
            throw new IssueNotFoundException(issueId);

        if (issue.getToken() == null)
            throw new IssueIsClosedException(issueId);

        issue.setToken(null);
        issue.setStatus(IssueConstant.STATUS_DENY);
        issueRepository.save(issue);

        // TODO: send email deny
    }
}


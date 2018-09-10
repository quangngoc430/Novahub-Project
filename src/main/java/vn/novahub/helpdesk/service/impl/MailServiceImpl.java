package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.enums.IssueEnum;
import vn.novahub.helpdesk.exception.AccountNotFoundException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Issue;
import vn.novahub.helpdesk.model.Mail;
import vn.novahub.helpdesk.repository.AccountRepository;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.enums.RoleEnum;
import vn.novahub.helpdesk.service.MailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.ArrayList;

@Service
@PropertySource("classpath:email.properties")
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private Environment env;

    @Value("${host_url}")
    private String hostUrl;

    @Override
    @Async
    public void sendSimpleMail(Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(mail.getEmailReceiving());
        message.setSubject(mail.getSubject());
        message.setText(mail.getContent());

        mailSender.send(message);
    }

    @Override
    @Async
    public void sendHTMLMail(Mail mail) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");

        helper.setTo(mail.getEmailReceiving());
        helper.setSubject(mail.getSubject());
        message.setContent(mail.getContent(), "text/html");

        mailSender.send(message);
    }

    @Override
    public String getContentMail(String fileName) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:templates/emails/" + fileName);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        String line;
        String content = "";
        while ((line = buffer.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }

            while(line.charAt(0) == ' '){
                if(line.length() > 1)
                    line = line.substring(1);
                else {
                    line = "";
                    break;
                }
            }
            content += line;
        }
        return content;
    }

    @Override
    @Async
    public void sendMailCreateIssueForAdminAndClerk(Issue issue, Account accountLogin) throws MessagingException, IOException {
        Mail mailForAdmin = new Mail();
        String subjectForAdmin = env.getProperty("subject_email_create_issue");
        subjectForAdmin = subjectForAdmin.replace("{issue-id}", String.valueOf(issue.getId()));
        mailForAdmin.setSubject(subjectForAdmin);
        String contentForAdmin = getContentMail("create_issue.html");
        contentForAdmin = contentForAdmin.replace("{issue-id}", String.valueOf(issue.getId()));
        contentForAdmin = contentForAdmin.replace("{email}", accountLogin.getEmail());
        contentForAdmin = contentForAdmin.replace("{title}", issue.getTitle());
        contentForAdmin = contentForAdmin.replace("{content}", issue.getContent());
        contentForAdmin = contentForAdmin.replace("{status}", issue.getStatus());
        contentForAdmin = contentForAdmin.replace("{reply-message}", (issue.getReplyMessage() == null) ? IssueEnum.NONE.name() : issue.getReplyMessage());
        contentForAdmin = contentForAdmin.replace("{url-approve-issue}", hostUrl + "/api/issues/" + issue.getId() + "/action?status=APPROVE&token=" + issue.getToken());
        contentForAdmin = contentForAdmin.replace("{url-deny-issue}", hostUrl + "/api/issues/" + issue.getId() + "/action?status=DENY&token=" + issue.getToken());
        mailForAdmin.setContent(contentForAdmin);

        mailForAdmin.setEmailReceiving(accountService.getAllEmailsOfAdmin().toArray(new String[0]));
        sendHTMLMail(mailForAdmin);


        Mail mailForClerk = new Mail();
        String subjectForClerk = env.getProperty("subject_email_create_issue");
        subjectForClerk = subjectForClerk.replace("{issue-id}", String.valueOf(issue.getId()));
        mailForClerk.setSubject(subjectForClerk);
        String contentForClerk = getContentMail("create_issue.html");
        contentForClerk = contentForClerk.replace("{issue-id}", String.valueOf(issue.getId()));
        contentForClerk = contentForClerk.replace("{email}", accountLogin.getEmail());
        contentForClerk = contentForClerk.replace("{title}", issue.getTitle());
        contentForClerk = contentForClerk.replace("{content}", issue.getContent());
        contentForClerk = contentForClerk.replace("{status}", issue.getStatus());
        contentForClerk = contentForClerk.replace("{reply-message}", (issue.getReplyMessage() == null) ? IssueEnum.NONE.name() : issue.getReplyMessage());
        mailForClerk.setContent(contentForClerk);

        mailForClerk.setEmailReceiving(accountService.getAllEmailsOfClerk().toArray(new String[0]));
        sendHTMLMail(mailForClerk);
    }

    @Override
    @Async
    public void sendMailUpdateIssueForAdmin(Issue issue) throws MessagingException, IOException, AccountNotFoundException {
        Optional<Account> accountOptional = accountRepository.findById(issue.getAccountId());

        if(!accountOptional.isPresent())
            throw new AccountNotFoundException(issue.getAccountId());

        Account accountLogin = accountOptional.get();

        Mail mail = new Mail();
        String subject = env.getProperty("subject_email_update_issue_admin");
        subject = subject.replace("{issue-id}", String.valueOf(issue.getId()));
        mail.setSubject(subject);
        String content = getContentMail("update_issue_admin.html");
        content = content.replace("{issue-id}", String.valueOf(issue.getId()));
        content = content.replace("{email}", accountLogin.getEmail());
        content = content.replace("{title}", issue.getTitle());
        content = content.replace("{content}", issue.getContent());
        content = content.replace("{status}", issue.getStatus());
        content = content.replace("{reply-message}", (issue.getReplyMessage() == null) ? IssueEnum.NONE.name() : issue.getReplyMessage());
        mail.setContent(content);

        mail.setEmailReceiving(accountService.getAllEmailsOfAdmin().toArray(new String[0]));

        sendHTMLMail(mail);
    }

    @Override
    @Async
    public void sendMailUpdateIssueForUser(Issue issue) throws MessagingException, IOException {
        Optional<Account> accountOptional = accountRepository.findById(issue.getAccountId());

        Account account = accountOptional.get();

        Mail mail = new Mail();
        String subject = env.getProperty("subject_email_update_issue_account");
        subject = subject.replace("{issue-id}", String.valueOf(issue.getId()));
        mail.setSubject(subject);
        String content = getContentMail("update_issue_account.html");
        content = content.replace("{issue-id}", String.valueOf(issue.getId()));
        content = content.replace("{email}", account.getEmail());
        content = content.replace("{title}", issue.getTitle());
        content = content.replace("{content}", issue.getContent());
        content = content.replace("{status}", issue.getStatus());
        content = content.replace("{reply-message}", (issue.getReplyMessage() == null) ? "NONE" : issue.getReplyMessage());
        mail.setContent(content);
        mail.setEmailReceiving(new String[]{account.getEmail()});

        sendHTMLMail(mail);
    }

    @Override
    @Async
    public void sendMailUpdateIssueForClerk(Issue issue) throws IOException, MessagingException {
        Optional<Account> accountOptional = accountRepository.findById(issue.getAccountId());

        Account account = accountOptional.get();

        Mail mail = new Mail();
        String subject = env.getProperty("subject_email_update_issue_account");
        subject = subject.replace("{issue-id}", String.valueOf(issue.getId()));
        mail.setSubject(subject);
        String content = getContentMail("update_issue_account.html");
        content = content.replace("{issue-id}", String.valueOf(issue.getId()));
        content = content.replace("{email}", account.getEmail());
        content = content.replace("{title}", issue.getTitle());
        content = content.replace("{content}", issue.getContent());
        content = content.replace("{status}", issue.getStatus());
        content = content.replace("{reply-message}", (issue.getReplyMessage() == null) ? "NONE" : issue.getReplyMessage());
        mail.setContent(content);
        ArrayList<String> emails = new ArrayList<>();
        mail.setEmailReceiving(getEmails(RoleEnum.CLERK.name()).toArray(new String[0]));

        sendHTMLMail(mail);
    }

    public ArrayList<String> getEmailsOfAdminAndClerk() {
        ArrayList<String> emails = new ArrayList<>();

        if (getEmails(RoleEnum.ADMIN.name()) != null) {
            emails.addAll(getEmails(RoleEnum.ADMIN.name()));
        }

        if (getEmails(RoleEnum.CLERK.name()) != null) {
            emails.addAll(getEmails(RoleEnum.CLERK.name()));
        }

        return emails;
    }

    @Override
    public ArrayList<String> getEmails(String role) {
        ArrayList<String> emails = new ArrayList<>();
        ArrayList<Account> accounts = (ArrayList<Account>)
                (accountRepository.getAllByRoleName(role));

        if (accounts != null) {
            for (Account account : accounts) {
                emails.add(account.getEmail());
            }
        }

        return emails;
    }
}

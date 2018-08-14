package vn.novahub.helpdesk.service;

import vn.novahub.helpdesk.exception.AccountNotFoundException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Issue;
import vn.novahub.helpdesk.model.Mail;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;

public interface MailService {

    void sendSimpleMail(Mail mail);

    void sendHTMLMail(Mail mail) throws MessagingException;

    String getContentMail(String fileName) throws IOException;

    void sendMailCreateIssueForAdminAndClerk(Issue issue, Account accountLogin) throws MessagingException, IOException;

    void sendMailUpdateIssueForAdmin(Issue issue) throws MessagingException, IOException, AccountNotFoundException;

    void sendMailUpdateIssueForUser(Issue issue) throws MessagingException, IOException;

    void sendMailUpdateIssueForClerk(Issue issue) throws IOException, MessagingException;

    ArrayList<String> getEmailsOfAdminAndClerk();

    ArrayList<String> getEmails(String role);

}

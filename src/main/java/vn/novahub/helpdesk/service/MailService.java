package vn.novahub.helpdesk.service;

import vn.novahub.helpdesk.model.Mail;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;

public interface MailService {

    void sendSimpleMail(Mail mail);

    void sendHTMLMail(Mail mail) throws MessagingException;

    String getContentMail(String fileName) throws IOException;

    ArrayList<String> getEmailsOfAdminAndClerk();

    ArrayList<String> getEmails(String role);

}

package vn.novahub.helpdesk.service;

import vn.novahub.helpdesk.model.Mail;

import javax.mail.MessagingException;

public interface MailService {

    void sendSimpleMail(Mail mail);

    void sendHTMLMail(Mail mail) throws MessagingException;
}

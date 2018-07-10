package vn.novahub.helpdesk.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.model.Mail;
import vn.novahub.helpdesk.service.MailService;

import javax.mail.MessagingException;

@Service
public class MailThread extends Thread {

    @Autowired
    private MailService mailService;

    private Mail mail;

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    @Override
    public synchronized void run(){
        try {
            mailService.sendHTMLMail(mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}

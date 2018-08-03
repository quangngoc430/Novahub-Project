package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.enums.RoleEnum;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Mail;
import vn.novahub.helpdesk.repository.AccountRepository;
import vn.novahub.helpdesk.service.MailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private AccountRepository accountRepository;

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
    public ArrayList<String> getEmailsOfAdminAndClerk() {
        ArrayList<Account> adminList = (ArrayList<Account>)
                (accountRepository.getAllByRoleName(RoleEnum.ADMIN.name()));
        ArrayList<Account> clerkList = (ArrayList<Account>)
                (accountRepository.getAllByRoleName(RoleEnum.CLERK.name()));

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

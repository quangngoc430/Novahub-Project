package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.model.Mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class MailServiceImpl implements MailService{

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public void sendSimpleMail(Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(mail.getEmailReceiving());
        message.setSubject(mail.getSubject());
        message.setText(mail.getContent());

        mailSender.send(message);
    }

    @Override
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
}

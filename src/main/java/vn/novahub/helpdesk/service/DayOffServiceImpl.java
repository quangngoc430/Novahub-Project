package vn.novahub.helpdesk.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.constant.DayOffConstant;
import vn.novahub.helpdesk.constant.RoleConstant;
import vn.novahub.helpdesk.exception.DayOffIsAnsweredException;
import vn.novahub.helpdesk.exception.DayOffOverdueException;
import vn.novahub.helpdesk.exception.DayOffTokenIsNotMatchException;
import vn.novahub.helpdesk.exception.DayOffTypeIsNotValidException;
import vn.novahub.helpdesk.model.*;
import vn.novahub.helpdesk.repository.AccountRepository;
import vn.novahub.helpdesk.repository.DayOffRepository;
import vn.novahub.helpdesk.repository.DayOffTypeRepository;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
public class DayOffServiceImpl implements DayOffService{

    @Autowired
    private DayOffRepository dayOffRepository;

    @Autowired
    private DayOffTypeRepository dayOffTypeRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private Environment env;

    @Autowired
    private MailService mailService;

    @Autowired
    private DayOffTypeFactory dayOffTypeFactory;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public DayOff add(DayOff dayOff) throws MessagingException, DayOffTypeIsNotValidException{
        Account accountLogin = accountService.getAccountLogin();

        validateDayOffType(dayOff);

        initialize(dayOff);

        DayOff newDayOff = dayOffRepository.save(dayOff);

        sendMailCreateDayOff(newDayOff, accountLogin);

        return newDayOff;
    }

    @Override
    public void delete(DayOff dayOff)
            throws MessagingException,
                    DayOffOverdueException {
        Date currentDate = new Date();
        dayOff = dayOffRepository.getById(dayOff.getId());

        if (currentDate.before(dayOff.getStartDate())) {
            dayOffRepository.delete(dayOff);
        } else {
            throw new DayOffOverdueException(dayOff.getId());
        }
        sendMailDeleteForAdmin(dayOff);
    }

    @Override
    public void approve(long dayOffId, String token)
            throws DayOffIsAnsweredException, DayOffTokenIsNotMatchException, MessagingException{

        DayOff dayOff = checkIfRequestIsAnswered(dayOffId, token);

        if (dayOff.getToken().equals(token)) {
            dayOff.getDayOffType().subtractRemainingTime(dayOff.getNumberOfHours());
            answerDayOffRequest(dayOff, DayOffConstant.STATUS_APPROVE);
        } else {
            throw new DayOffTokenIsNotMatchException();
        }
    }

    @Override
    public void deny(long dayOffId, String token)
            throws DayOffIsAnsweredException, DayOffTokenIsNotMatchException, MessagingException {
        DayOff dayOff = checkIfRequestIsAnswered(dayOffId, token);

        if (dayOff.getToken().equals(token)) {
            answerDayOffRequest(dayOff, DayOffConstant.STATUS_DENY);
        } else {
            throw new DayOffTokenIsNotMatchException();
        }
    }

    private void answerDayOffRequest(DayOff dayOff, String status) throws MessagingException{
        dayOff.setToken("");
        dayOff.setStatus(status);
        dayOffRepository.save(dayOff);

        sendMailUpdateForUser(dayOff);
    }

    private DayOff checkIfRequestIsAnswered(long dayOffId, String token) throws DayOffIsAnsweredException{
        DayOff dayOff = dayOffRepository.getById(dayOffId);

        if (dayOff.getToken().trim().isEmpty()) {
            throw new DayOffIsAnsweredException(dayOffId);
        }
        return dayOff;
    }

    private void validateDayOffType(DayOff dayOff) throws DayOffTypeIsNotValidException{
        int createdYear = getCreatedYear(new Date());
        Account accountLogin = accountService.getAccountLogin();

        DayOffType dayOffType = dayOffTypeRepository
                .findByAccountIdAndTypeAndYear(
                        accountLogin.getId(),
                        dayOff.getType(),
                        createdYear);

        if (dayOffType == null) {
            logger.info("Day off type null");
            dayOffType = dayOffTypeFactory.create(dayOff.getType());
            dayOffType.setAccountId(accountLogin.getId());
            dayOffType.setYear(createdYear);
            dayOffType = dayOffTypeRepository.save(dayOffType);
        }
        dayOff.setDayOffType(dayOffType);
        dayOff.setTypeId(dayOffType.getId());
    }

    private void initialize(DayOff dayOff) {
        Account accountLogin = accountService.getAccountLogin();
        Date createdDate = new Date();

        dayOff.setCreatedAt(createdDate);
        dayOff.setUpdatedAt(createdDate);
        dayOff.setStatus(DayOffConstant.STATUS_PENDING);
        dayOff.setToken(tokenService.generateToken(accountLogin.getId() + dayOff.getTitle()));
        dayOff.setAccountId(accountLogin.getId());
    }


    private void sendMailCreateDayOff(DayOff dayOff, Account accountLogin) throws MessagingException {
        Mail mail = new Mail();
        String subject = env.getProperty("subject_email_create_day_off");
        subject = subject.replace("{day-off-id}", String.valueOf(dayOff.getId()));
        mail.setSubject(subject);
        String content = env.getProperty("content_email_create_day_off");
        content = content.replace("{day-off-id}", String.valueOf(dayOff.getId()));
        content = content.replace("{email}", accountLogin.getEmail());
        content = content.replace("{title}", dayOff.getTitle());
        content = content.replace("{status}", dayOff.getStatus());
        content = content.replace("{url-approve-day-off}", "http://localhost:8080/api/day-offs/" + dayOff.getId() + "/approve?token=" + dayOff.getToken());
        content = content.replace("{url-deny-day-off}", "http://localhost:8080/api/day-offs/" + dayOff.getId() + "/deny?token=" + dayOff.getToken());
        mail.setContent(content);

        mail.setEmailReceiving(getEmailListOfAdmin().toArray(new String[0]));

        mailService.sendHTMLMail(mail);
    }

    private void sendMailUpdateForUser(DayOff dayOff) throws MessagingException{
        Account account = accountRepository.getById(dayOff.getAccountId());
        Mail mail = new Mail();
        String subject = env.getProperty("subject_email_update_day_off_account");
        subject = subject.replace("{day-off-id}", String.valueOf(dayOff.getId()));
        mail.setSubject(subject);

        String content = env.getProperty("content_email_update_day_off_account");
        content = content.replace("{day-off-id}", String.valueOf(dayOff.getId()));
        content = content.replace("{email}", account.getEmail());
        content = content.replace("{title}", dayOff.getTitle());
        content = content.replace("{status}", dayOff.getStatus());
        mail.setContent(content);
        mail.setEmailReceiving(new String[]{account.getEmail()});

        mailService.sendHTMLMail(mail);

    }

    private void sendMailDeleteForAdmin(DayOff dayOff) throws MessagingException{
        Account account = accountRepository.getById(dayOff.getAccountId());
        Mail mail = new Mail();
        String subject = env.getProperty("subject_email_delete_day_off");
        subject = subject.replace("{day-off-id}", String.valueOf(dayOff.getId()));
        mail.setSubject(subject);

        String content = env.getProperty("content_email_delete_day_off");
        content = content.replace("{day-off-id}", String.valueOf(dayOff.getId()));
        content = content.replace("{email}", account.getEmail());
        content = content.replace("{title}", dayOff.getTitle());
        content = content.replace("{status}", dayOff.getStatus());
        mail.setContent(content);

        mail.setEmailReceiving(getEmailListOfAdmin().toArray(new String[0]));

        mailService.sendHTMLMail(mail);
    }

    private ArrayList<String> getEmailListOfAdmin() {
        ArrayList<Account> adminList = (ArrayList<Account>)
                (accountRepository.getAllByRoleName(RoleConstant.ROLE_ADMIN));
        ArrayList<Account> clerkList = (ArrayList<Account>)
                (accountRepository.getAllByRoleName(RoleConstant.ROLE_CLERK));

        ArrayList<String> emails = new ArrayList<>();

        if(adminList != null)
            for (Account account : adminList)
                emails.add(account.getEmail());

        if(clerkList != null)
            for (Account account : clerkList)
                emails.add(account.getEmail());
        return emails;
    }

    private int getCreatedYear(Date createdDate) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(createdDate);
        return calendar.get(Calendar.YEAR);
    }


}

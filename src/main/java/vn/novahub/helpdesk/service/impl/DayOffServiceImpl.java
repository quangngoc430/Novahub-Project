package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.constant.DayOffConstant;
import vn.novahub.helpdesk.enums.RoleEnum;
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.*;
import vn.novahub.helpdesk.repository.AccountRepository;
import vn.novahub.helpdesk.repository.DayOffRepository;
import vn.novahub.helpdesk.repository.DayOffTypeRepository;
import vn.novahub.helpdesk.service.*;

import javax.mail.MessagingException;
import java.util.*;

@Service
public class DayOffServiceImpl implements DayOffService {

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

    @Override
    public Page<DayOff> getAllByAccountIdAndTypeAndStatus(
            long accountId,
            String typeKeyword,
            String statusKeyword,
            Pageable pageable) {

        typeKeyword = "%" + typeKeyword + "%";
        statusKeyword = "%" + statusKeyword + "%";

        if (accountId == 0) {
            return dayOffRepository.getAllByTypeLikeAndStatusLike(
                    typeKeyword,
                    statusKeyword,
                    pageable);
        }

        return dayOffRepository.getAllByAccountIdAndTypeLikeAndStatusLike(
                accountId,
                typeKeyword,
                statusKeyword,
                pageable);
    }

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
    public DayOff delete(long dayOffId)
            throws MessagingException,
                    DayOffOverdueException,
                    DayOffIsNotExistException,
                    UnauthorizedException,
                    AccountNotFoundException {

        Date currentDate = new Date();
        Optional<DayOff> dayOff = dayOffRepository.findById(dayOffId);

        if (!dayOff.isPresent()) {
            throw new DayOffIsNotExistException(dayOffId);
        }

        Account account = accountService.getAccountLogin();
        if (account.getId() != dayOff.get().getAccountId()) {
            throw new UnauthorizedException("This is not your day off", "/api/day-offs");
        }

        if (currentDate.before(dayOff.get().getStartDate())) {
            dayOffRepository.delete(dayOff.get());
        } else {
            throw new DayOffOverdueException(dayOffId);
        }

        sendMailDeleteForAdmin(dayOff.get());

        return dayOff.get();
    }

    @Override
    public DayOff approve(long dayOffId, String token)
            throws DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            DayOffIsNotExistException,
            MessagingException,
            AccountNotFoundException {

        DayOff dayOff = checkIfRequestIsAnswered(dayOffId, token);

        if (dayOff.getToken().equals(token)) {
            dayOff.getDayOffType().subtractRemainingTime(dayOff.getNumberOfHours());
            answerDayOffRequest(dayOff, DayOffConstant.STATUS_APPROVE);
        } else {
            throw new DayOffTokenIsNotMatchException();
        }

        return dayOff;
    }

    @Override
    public DayOff deny(long dayOffId, String token)
            throws DayOffIsAnsweredException,
            DayOffIsNotExistException,
            DayOffTokenIsNotMatchException,
            MessagingException,
            AccountNotFoundException {

        DayOff dayOff = checkIfRequestIsAnswered(dayOffId, token);

        if (dayOff.getToken().equals(token)) {
            answerDayOffRequest(dayOff, DayOffConstant.STATUS_DENY);
        } else {
            throw new DayOffTokenIsNotMatchException();
        }
        return dayOff;
    }

    private void answerDayOffRequest(DayOff dayOff, String status) throws MessagingException, AccountNotFoundException {
        dayOff.setToken("");
        dayOff.setStatus(status);
        dayOffRepository.save(dayOff);

        sendMailUpdateForUser(dayOff);
    }

    private DayOff checkIfRequestIsAnswered(long dayOffId, String token)
            throws DayOffIsAnsweredException,
                   DayOffIsNotExistException {
        Optional<DayOff> dayOff = dayOffRepository.findById(dayOffId);

        if (!dayOff.isPresent()) {
            throw new DayOffIsNotExistException(dayOffId);
        }

        if (dayOff.get().getToken().trim().isEmpty()) {
            throw new DayOffIsAnsweredException(dayOffId);
        }
        return dayOff.get();
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
        dayOff.setToken(tokenService.generateToken(accountLogin.getId() + dayOff.getComment()));
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
        content = content.replace("{title}", dayOff.getComment());
        content = content.replace("{status}", dayOff.getStatus());
        content = content.replace("{url-approve-day-off}", "http://localhost:8080/api/day-offs/" + dayOff.getId() + "/approve?token=" + dayOff.getToken());
        content = content.replace("{url-deny-day-off}", "http://localhost:8080/api/day-offs/" + dayOff.getId() + "/deny?token=" + dayOff.getToken());
        mail.setContent(content);

        mail.setEmailReceiving(getEmailListOfAdmin().toArray(new String[0]));

        mailService.sendHTMLMail(mail);
    }

    private void sendMailUpdateForUser(DayOff dayOff) throws MessagingException, AccountNotFoundException {
        Optional<Account> accountOptional = accountRepository.findById(dayOff.getAccountId());

        if(!accountOptional.isPresent())
            throw new AccountNotFoundException(dayOff.getAccountId());

        Account account = accountOptional.get();
        Mail mail = new Mail();
        String subject = env.getProperty("subject_email_update_day_off_account");
        subject = subject.replace("{day-off-id}", String.valueOf(dayOff.getId()));
        mail.setSubject(subject);

        String content = env.getProperty("content_email_update_day_off_account");
        content = content.replace("{day-off-id}", String.valueOf(dayOff.getId()));
        content = content.replace("{email}", account.getEmail());
        content = content.replace("{title}", dayOff.getComment());
        content = content.replace("{status}", dayOff.getStatus());
        mail.setContent(content);
        mail.setEmailReceiving(new String[]{account.getEmail()});

        mailService.sendHTMLMail(mail);

    }

    private void sendMailDeleteForAdmin(DayOff dayOff) throws MessagingException, AccountNotFoundException {
        Optional<Account> accountOptional = accountRepository.findById(dayOff.getAccountId());

        if(!accountOptional.isPresent())
            throw new AccountNotFoundException(dayOff.getAccountId());

        Account account = accountOptional.get();
        Mail mail = new Mail();
        String subject = env.getProperty("subject_email_delete_day_off");
        subject = subject.replace("{day-off-id}", String.valueOf(dayOff.getId()));
        mail.setSubject(subject);

        String content = env.getProperty("content_email_delete_day_off");
        content = content.replace("{day-off-id}", String.valueOf(dayOff.getId()));
        content = content.replace("{email}", account.getEmail());
        content = content.replace("{title}", dayOff.getComment());
        content = content.replace("{status}", dayOff.getStatus());
        mail.setContent(content);

        mail.setEmailReceiving(getEmailListOfAdmin().toArray(new String[0]));

        mailService.sendHTMLMail(mail);
    }

    private ArrayList<String> getEmailListOfAdmin() {
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

    private int getCreatedYear(Date createdDate) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(createdDate);
        return calendar.get(Calendar.YEAR);
    }


}

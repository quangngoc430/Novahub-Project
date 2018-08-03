package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.enums.DayOffEnum;
import vn.novahub.helpdesk.enums.RoleEnum;
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.*;
import vn.novahub.helpdesk.repository.AccountRepository;
import vn.novahub.helpdesk.repository.CommonDayOffTypeRepository;
import vn.novahub.helpdesk.repository.DayOffRepository;
import vn.novahub.helpdesk.repository.DayOffTypeRepository;
import vn.novahub.helpdesk.service.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DayOffServiceImpl implements DayOffService {



    @Autowired
    private DayOffRepository dayOffRepository;

    @Autowired
    private DayOffTypeRepository dayOffTypeRepository;

    @Autowired
    private CommonDayOffTypeRepository commonDayOffTypeRepository;

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

    @Override
    public DayOff add(DayOff dayOff)
            throws MessagingException,
            CommonTypeIsNotExistException,
            IOException {
        Account accountLogin = accountService.getAccountLogin();

        initialize(dayOff);

        DayOff newDayOff = dayOffRepository.save(dayOff);

        sendEmailDayOff(dayOff, accountLogin, getEmailListOfAdmin().toArray(new String[0]));

        return newDayOff;
    }

    @Override
    public Page<DayOff> getAllByAccountIdAndStatus(long accountId, String status, Pageable pageable) {
        if (status.equals("NON-CANCELLED")) {
            return dayOffRepository.findNonCancelledByAccountId(accountId, pageable);
        } else if (status.equals("")) {
            return dayOffRepository.findByAccountId(accountId, pageable);
        } else {
            return dayOffRepository.findByAccountIdAndStatus(accountId, status, pageable);
        }
    }

    @Override
    public DayOff delete(long dayOffId)
            throws MessagingException,
            DayOffOverdueException,
            DayOffIsNotExistException,
            UnauthorizedException,
            AccountNotFoundException {
        return null;
    }

    @Override
    public DayOff approve(long dayOffId, String token)
            throws DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            DayOffIsNotExistException,
            MessagingException,
            AccountNotFoundException {
        return null;
    }

    @Override
    public DayOff deny(long dayOffId, String token)
            throws DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            DayOffIsNotExistException,
            MessagingException,
            AccountNotFoundException {
        return null;
    }

    private void initialize(DayOff dayOff) throws CommonTypeIsNotExistException {

        CommonDayOffType commonDayOffType = checkIfCommonTypeIsExist(dayOff);

        DayOffType dayOffType = updateOrCreateDayOffType(commonDayOffType, dayOff.getDayOffType().getYear());

        setDefaultField(dayOff);

        dayOff.setDayOffType(dayOffType);

        dayOff.setTypeId(dayOffType.getId());

    }

    private CommonDayOffType checkIfCommonTypeIsExist(DayOff dayOff)
                                            throws CommonTypeIsNotExistException {
        Optional<CommonDayOffType> commonDayOffType =
                commonDayOffTypeRepository.findById(dayOff.getDayOffType().getCommonTypeId());
        if (!commonDayOffType.isPresent()) {
            throw new CommonTypeIsNotExistException();
        }
        return commonDayOffType.get();
    }

    private DayOffType updateOrCreateDayOffType(CommonDayOffType commonDayOffType, int year) {
        Account accountLogin = accountService.getAccountLogin();
        return dayOffTypeRepository
                .findByAccountIdAndCommonTypeIdAndYear(
                        accountLogin.getId(),
                        commonDayOffType.getId(),
                        year);
    }

    private void setDefaultField(DayOff dayOff) {
        Account accountLogin = accountService.getAccountLogin();
        LocalDateTime createdDate = LocalDateTime.now();
        dayOff.setCreatedAt(createdDate);
        dayOff.setUpdatedAt(createdDate);
        dayOff.setStatus(DayOffEnum.PENDING.name());
        dayOff.setToken(tokenService.generateToken(accountLogin.getId() + dayOff.getComment()));
        dayOff.setAccountId(accountLogin.getId());
    }

    private void sendEmailDayOff(DayOff dayOff, Account account, String[] emailList)
            throws MessagingException, IOException {
        Account accountLogin = accountService.getAccountLogin();
        Mail mail = new Mail();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy");
        String dayOffMailName = env.getProperty("day_off_mail_name");
        String hostUrl = env.getProperty("host_url");

        String subject = "Day off request of " + accountLogin.getEmail() + " has been " + dayOff.getStatus();
        mail.setSubject(subject);
        String content = mailService.getContentMail(dayOffMailName);
        content = content.replace("{email}", account.getEmail());
        content = content.replace("{type}", dayOff.getDayOffType().getCommonDayOffType().getType());
        content = content.replace("{start-date}", dayOff.getStartDate().format(formatter));
        content = content.replace("{end-date}", dayOff.getEndDate().format(formatter));
        content = content.replace("{number-of-hours}", dayOff.getNumberOfHours()+"");
        content = content.replace("{comment}", dayOff.getComment());
        content = content.replace("{remaining-hours}", dayOff.getDayOffType().getRemainingTime() + "");
        content = content.replace("{url-approve-day-off}", hostUrl+"/api/day-offs/" + dayOff.getId() + "/approve?token=" + dayOff.getToken());
        content = content.replace("{url-deny-day-off}", hostUrl+"/api/day-offs/" + dayOff.getId() + "/deny?token=" + dayOff.getToken());

        mail.setContent(content);

        mail.setEmailReceiving(emailList);

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


//
//    @Override
//    public Page<DayOff> getAllByAccountIdAndTypeAndStatus(
//            long accountId,
//            String typeKeyword,
//            String statusKeyword,
//            Pageable pageable) {
//
//        typeKeyword = "%" + typeKeyword + "%";
//        statusKeyword = "%" + statusKeyword + "%";
//
//        if (accountId == 0) {
//            return dayOffRepository.getAllByTypeLikeAndStatusLike(
//                    typeKeyword,
//                    statusKeyword,
//                    pageable);
//        }
//
//        return dayOffRepository.getAllByAccountIdAndTypeLikeAndStatusLike(
//                accountId,
//                typeKeyword,
//                statusKeyword,
//                pageable);
//    }
//

//
//    @Override
//    public DayOff delete(long dayOffId)
//            throws MessagingException,
//                    DayOffOverdueException,
//                    DayOffIsNotExistException,
//                    UnauthorizedException,
//                    AccountNotFoundException {
//
//        Date currentDate = new Date();
//        Optional<DayOff> dayOff = dayOffRepository.findById(dayOffId);
//
//        if (!dayOff.isPresent()) {
//            throw new DayOffIsNotExistException(dayOffId);
//        }
//
//        Account account = accountService.getAccountLogin();
//        if (account.getId() != dayOff.get().getAccountId()) {
//            throw new UnauthorizedException("This is not your day off", "/api/day-offs");
//        }
//
//        if (currentDate.before(dayOff.get().getStartDate())) {
//            dayOffRepository.delete(dayOff.get());
//        } else {
//            throw new DayOffOverdueException(dayOffId);
//        }
//
//        sendMailDeleteForAdmin(dayOff.get());
//
//        return dayOff.get();
//    }
//
//    @Override
//    public DayOff approve(long dayOffId, String token)
//            throws DayOffIsAnsweredException,
//            DayOffTokenIsNotMatchException,
//            DayOffIsNotExistException,
//            MessagingException,
//            AccountNotFoundException {
//
//        DayOff dayOff = checkIfRequestIsAnswered(dayOffId, token);
//
//        if (dayOff.getToken().equals(token)) {
//            dayOff.getDayOffType().subtractRemainingTime(dayOff.getNumberOfHours());
//            answerDayOffRequest(dayOff, DayOffConstant.STATUS_APPROVE);
//        } else {
//            throw new DayOffTokenIsNotMatchException();
//        }
//
//        return dayOff;
//    }
//
//    @Override
//    public DayOff deny(long dayOffId, String token)
//            throws DayOffIsAnsweredException,
//            DayOffIsNotExistException,
//            DayOffTokenIsNotMatchException,
//            MessagingException,
//            AccountNotFoundException {
//
//        DayOff dayOff = checkIfRequestIsAnswered(dayOffId, token);
//
//        if (dayOff.getToken().equals(token)) {
//            answerDayOffRequest(dayOff, DayOffConstant.STATUS_DENY);
//        } else {
//            throw new DayOffTokenIsNotMatchException();
//        }
//        return dayOff;
//    }
//
//    private void answerDayOffRequest(DayOff dayOff, String status) throws MessagingException, AccountNotFoundException {
//        dayOff.setToken("");
//        dayOff.setStatus(status);
//        dayOffRepository.save(dayOff);
//
//        sendMailUpdateForUser(dayOff);
//    }
//
//    private DayOff checkIfRequestIsAnswered(long dayOffId, String token)
//            throws DayOffIsAnsweredException,
//                   DayOffIsNotExistException {
//        Optional<DayOff> dayOff = dayOffRepository.findById(dayOffId);
//
//        if (!dayOff.isPresent()) {
//            throw new DayOffIsNotExistException(dayOffId);
//        }
//
//        if (dayOff.get().getToken().trim().isEmpty()) {
//            throw new DayOffIsAnsweredException(dayOffId);
//        }
//        return dayOff.get();
//    }
//
//    private void validateDayOffType(DayOff dayOff) throws DayOffTypeIsNotValidException{
//        int createdYear = getCreatedYear(new Date());
//        Account accountLogin = accountService.getAccountLogin();
//
//        DayOffType dayOffType = dayOffTypeRepository
//                .findByAccountIdAndTypeAndYear(
//                        accountLogin.getId(),
//                        dayOff.getType(),
//                        createdYear);
//
//        if (dayOffType == null) {
//            dayOffType = dayOffTypeFactory.create(dayOff.getType());
//            dayOffType.setAccountId(accountLogin.getId());
//            dayOffType.setYear(createdYear);
//            dayOffType = dayOffTypeRepository.save(dayOffType);
//        }
//        dayOff.setDayOffType(dayOffType);
//        dayOff.setTypeId(dayOffType.getId());
//    }
//
//    private void initialize(DayOff dayOff) {
//        Account accountLogin = accountService.getAccountLogin();
//        Date createdDate = new Date();
//
//        dayOff.setCreatedAt(createdDate);
//        dayOff.setUpdatedAt(createdDate);
//        dayOff.setStatus(DayOffConstant.STATUS_PENDING);
//        dayOff.setToken(tokenService.generateToken(accountLogin.getId() + dayOff.getComment()));
//        dayOff.setAccountId(accountLogin.getId());
//    }
//
//
//
//
//    private void sendMailUpdateForUser(DayOff dayOff) throws MessagingException, AccountNotFoundException {
//        Optional<Account> accountOptional = accountRepository.findById(dayOff.getAccountId());
//
//        if(!accountOptional.isPresent())
//            throw new AccountNotFoundException(dayOff.getAccountId());
//
//        Account account = accountOptional.get();
//        Mail mail = new Mail();
//        String subject = env.getProperty("subject_email_update_day_off_account");
//        subject = subject.replace("{day-off-id}", String.valueOf(dayOff.getId()));
//        mail.setSubject(subject);
//
//        String content = env.getProperty("content_email_update_day_off_account");
//        content = content.replace("{day-off-id}", String.valueOf(dayOff.getId()));
//        content = content.replace("{email}", account.getEmail());
//        content = content.replace("{title}", dayOff.getComment());
//        content = content.replace("{status}", dayOff.getStatus());
//        mail.setContent(content);
//        mail.setEmailReceiving(new String[]{account.getEmail()});
//
//        mailService.sendHTMLMail(mail);
//
//    }
//
//    private void sendMailDeleteForAdmin(DayOff dayOff) throws MessagingException, AccountNotFoundException {
//        Optional<Account> accountOptional = accountRepository.findById(dayOff.getAccountId());
//
//        if(!accountOptional.isPresent())
//            throw new AccountNotFoundException(dayOff.getAccountId());
//
//        Account account = accountOptional.get();
//        Mail mail = new Mail();
//        String subject = env.getProperty("subject_email_delete_day_off");
//        subject = subject.replace("{day-off-id}", String.valueOf(dayOff.getId()));
//        mail.setSubject(subject);
//
//        String content = env.getProperty("content_email_delete_day_off");
//        content = content.replace("{day-off-id}", String.valueOf(dayOff.getId()));
//        content = content.replace("{email}", account.getEmail());
//        content = content.replace("{title}", dayOff.getComment());
//        content = content.replace("{status}", dayOff.getStatus());
//        mail.setContent(content);
//
//        mail.setEmailReceiving(getEmailListOfAdmin().toArray(new String[0]));
//
//        mailService.sendHTMLMail(mail);
//    }
//

//
//    private int getCreatedYear(Date createdDate) {
//        Calendar calendar = new GregorianCalendar();
//        calendar.setTime(createdDate);
//        return calendar.get(Calendar.YEAR);
//    }


}

package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.enums.DayOffStatus;
import vn.novahub.helpdesk.enums.RoleEnum;
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.*;
import vn.novahub.helpdesk.repository.DayOffTypeRepository;
import vn.novahub.helpdesk.repository.DayOffRepository;
import vn.novahub.helpdesk.repository.DayOffAccountRepository;
import vn.novahub.helpdesk.service.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DayOffServiceImpl implements DayOffService {

    @Autowired
    private DayOffRepository dayOffRepository;

    @Autowired
    private DayOffAccountRepository dayOffAccountRepository;

    @Autowired
    private DayOffTypeRepository dayOffTypeRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private DayOffAccountService dayOffAccountService;

    @Autowired
    private Environment env;

    @Autowired
    private MailService mailService;

    @Override
    public DayOff add(DayOff dayOff)
            throws MessagingException,
            DayOffTypeNotFoundException,
            DayOffAccountIsExistException,
            AccountNotFoundException,
            IOException {

        initialize(dayOff);

        DayOff newDayOff = dayOffRepository.save(dayOff);

        sendEmailDayOff(dayOff, RoleEnum.ADMIN.name());

        sendEmailDayOff(dayOff, RoleEnum.CLERK.name());

        return newDayOff;
    }

    @Override
    public Page<DayOff> getAllByAccountIdAndStatus(long accountId, String status, Pageable pageable) {
        if (status.equals(DayOffStatus.NONCANCELLED.name())) {
            return dayOffRepository.findNonCancelledByAccountId(accountId, pageable);
        } else if (status.equals("")) {
            return dayOffRepository.findAllByAccountId(accountId, pageable);
        } else {
            return dayOffRepository.findByAccountIdAndStatus(accountId, status, pageable);
        }
    }

    @Override
    public Page<DayOff> getAllByAccountId(long accountId, Pageable pageable) {
        return dayOffRepository.findAllByAccountId(accountId, pageable);
    }

    @Override
    public Page<DayOff> getAllByStatusAndKeyword(String status, String keyword, Pageable pageable) {
        if (status.equals(DayOffStatus.NONCANCELLED.name())) {
            return dayOffRepository.findNonCancelledByKeyword(keyword, pageable);
        }

        return dayOffRepository.findByKeyword(keyword, pageable);

    }

    @Override
    public DayOff getById(long id) throws DayOffIsNotExistException, AccountNotFoundException {
        Optional<DayOff> dayOffOptional = dayOffRepository.findById(id);
        Account account = accountService.getAccountLogin();

        if (!dayOffOptional.isPresent()) {
            throw new DayOffIsNotExistException(id);
        }
        if (dayOffOptional.get().getDayOffAccount().getAccountId() != account.getId()
                && !account.getRole().getName().equals(RoleEnum.ADMIN.name())) {
            throw new AccountNotFoundException("Account is not admin or not own this day off");
        }

        return dayOffOptional.get();
    }

    @Override
    public DayOff approve(long dayOffId, String token)
            throws DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            DayOffIsNotExistException,
            MessagingException,
            AccountNotFoundException,
            IOException {

        DayOff dayOff = checkIfRequestIsAnswered(dayOffId);

        if (dayOff.getToken().equals(token)) {
            dayOff.getDayOffAccount().subtractRemainingTime(dayOff.getNumberOfHours());
            answerDayOffRequest(dayOff, DayOffStatus.APPROVED.name());
            sendEmailDayOff(dayOff, RoleEnum.EMPLOYEE.name());
            sendEmailDayOff(dayOff, RoleEnum.CLERK.name());
        } else {
            throw new DayOffTokenIsNotMatchException();
        }

        return dayOff;
    }

    @Override
    public DayOff deny(long dayOffId, String token)
            throws DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            DayOffIsNotExistException,
            MessagingException,
            AccountNotFoundException,
            IOException {
        DayOff dayOff = checkIfRequestIsAnswered(dayOffId);

        if (dayOff.getToken().equals(token)) {
            answerDayOffRequest(dayOff, DayOffStatus.DENIED.name());
        } else {
            throw new DayOffTokenIsNotMatchException();
        }

        sendEmailDayOff(dayOff, RoleEnum.EMPLOYEE.name());

        sendEmailDayOff(dayOff, RoleEnum.CLERK.name());

        return dayOff;
    }

    @Override
    public DayOff approve(long dayOffId)
            throws DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            DayOffIsNotExistException,
            MessagingException,
            AccountNotFoundException,
            IOException {

        DayOff dayOff = checkIfRequestIsAnswered(dayOffId);

        answerDayOffRequest(dayOff, DayOffStatus.APPROVED.name());

        sendEmailDayOff(dayOff, RoleEnum.EMPLOYEE.name());

        sendEmailDayOff(dayOff, RoleEnum.CLERK.name());

        return dayOff;
    }

    @Override
    public DayOff deny(long dayOffId)
            throws
            DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            DayOffIsNotExistException,
            MessagingException,
            AccountNotFoundException,
            IOException {

        DayOff dayOff = checkIfRequestIsAnswered(dayOffId);

        answerDayOffRequest(dayOff, DayOffStatus.DENIED.name());

        sendEmailDayOff(dayOff, RoleEnum.EMPLOYEE.name());

        sendEmailDayOff(dayOff, RoleEnum.CLERK.name());

        return dayOff;
    }

    @Override
    public DayOff cancel(long dayOffId)
            throws DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            DayOffIsNotExistException,
            MessagingException,
            AccountNotFoundException,
            DayOffOverdueException,
            IOException {

        Optional<DayOff> dayOffOptional = dayOffRepository.findById(dayOffId);

        if (!dayOffOptional.isPresent()) {
            throw new DayOffIsNotExistException(dayOffId);
        }

        if (dayOffOptional.get().getStatus().equals(DayOffStatus.CANCELLED.name())) {
            throw new DayOffIsAnsweredException(dayOffId);
        }

        if (LocalDate.now().isAfter(dayOffOptional.get().getStartDate())) {
            throw new DayOffOverdueException(dayOffId);
        }

        returnTheRemainingTime(dayOffOptional.get(), dayOffOptional.get().getDayOffAccount());

        DayOff dayOff = answerDayOffRequest(dayOffOptional.get(), DayOffStatus.CANCELLED.name());

        sendEmailDayOff(dayOff, RoleEnum.EMPLOYEE.name());
        sendEmailDayOff(dayOff, RoleEnum.CLERK.name());

        return dayOff;
    }


    private void initialize(DayOff dayOff) throws DayOffTypeNotFoundException, DayOffAccountIsExistException {

        DayOffType dayOffType = checkIfDayOffTypeIsExist(dayOff);

        DayOffAccount dayOffAccount = updateOrCreateDayOffAccount(dayOffType, dayOff.getDayOffAccount().getYear());

        setDefaultField(dayOff);

        dayOff.setDayOffAccount(dayOffAccount);

        dayOff.setDayOffAccountId(dayOffAccount.getId());

    }

    private DayOffType checkIfDayOffTypeIsExist(DayOff dayOff)
                                            throws DayOffTypeNotFoundException {
        Optional<DayOffType> dayOffTypeOptional =
                dayOffTypeRepository.findById(dayOff.getDayOffAccount().getDayOffTypeId());
        if (!dayOffTypeOptional.isPresent()) {
            throw new DayOffTypeNotFoundException();
        }
        return dayOffTypeOptional.get();
    }

    private DayOffAccount updateOrCreateDayOffAccount(DayOffType dayOffType, int year)
            throws DayOffAccountIsExistException,
            DayOffTypeNotFoundException {
        Account accountLogin = accountService.getAccountLogin();
        DayOffAccount dayOffAccount =  dayOffAccountRepository
                .findByAccountIdAndDayOffTypeIdAndYear(
                        accountLogin.getId(),
                        dayOffType.getId(),
                        year);
        if (dayOffAccount == null) {
            dayOffAccount = new DayOffAccount();
            dayOffAccount.setDayOffTypeId(dayOffType.getId());
            dayOffAccount.setAccountId(accountLogin.getId());
            return dayOffAccountService.add(dayOffAccount);
        }
        return dayOffAccount;
    }

    private void setDefaultField(DayOff dayOff) {
        Account accountLogin = accountService.getAccountLogin();
        LocalDateTime createdDate = LocalDateTime.now();
        dayOff.setCreatedAt(createdDate);
        dayOff.setUpdatedAt(createdDate);
        dayOff.setStatus(DayOffStatus.PENDING.name());
        dayOff.setToken(tokenService.generateToken(accountLogin.getId() + dayOff.getComment()));
    }

    private void sendEmailDayOff(DayOff dayOff, String receiversRole)
            throws MessagingException, IOException, AccountNotFoundException {
        Mail mail = new Mail();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy");
        String dayOffMailName = env.getProperty("day_off_mail_name");
        String hostUrl = env.getProperty("host_url");
        String[] emailList;
        Account account = accountService.get(dayOff.getDayOffAccount().getAccountId());

        String subject = "Day off request of " +
               account.getEmail() +
                " has been " + dayOff.getStatus();
        mail.setSubject(subject);
        String content = mailService.getContentMail(dayOffMailName);
        if (receiversRole.equals(RoleEnum.EMPLOYEE.name())) {
            content = content.replace("{email}", RoleEnum.ADMIN.name());
            emailList = new String[]{account.getEmail()};
        } else {
            content = content.replace("{email}", account.getEmail());
            emailList = mailService.getEmails(receiversRole).toArray(new String[0]);
        }
        content = content.replace("{type}", dayOff.getDayOffAccount().getDayOffType().getType());
        content = content.replace("{start-date}", dayOff.getStartDate().format(formatter));
        content = content.replace("{end-date}", dayOff.getEndDate().format(formatter));
        content = content.replace("{number-of-hours}", dayOff.getNumberOfHours()+"");
        content = content.replace("{comment}", dayOff.getComment());
        content = content.replace("{remaining-hours}", dayOff.getDayOffAccount().getRemainingTime() + "");
        if (!receiversRole.equals(RoleEnum.ADMIN.name())) {
             content = content.replace("<a href=\"{url-approve-day-off}\"style=\"margin-right: 10px;\"class=\"btn btn-approve\">Approve</a>", "");
             content = content.replace("<a href=\"{url-deny-day-off}\" style=\"margin-left: 10px;\" class=\"btn btn-deny\">Deny</a>", "");
        } else {
            content = content.replace("{url-approve-day-off}", hostUrl +
                    "/api/admin/day-offs/" +
                    dayOff.getId() +
                    "/answer-token?status=" + DayOffStatus.APPROVED.name() +
                    "&token=" + dayOff.getToken());
            content = content.replace("{url-deny-day-off}", hostUrl +
                    "/api/admin/day-offs/" +
                    dayOff.getId() +
                    "/answer-token?status=" + DayOffStatus.DENIED.name() +
                    "&token=" + dayOff.getToken());
        }

        mail.setContent(content);

        mail.setEmailReceiving(emailList);

        mailService.sendHTMLMail(mail);
    }

    private DayOff answerDayOffRequest(DayOff dayOff, String status) {
        dayOff.setToken("");
        dayOff.setStatus(status);
        dayOff.setUpdatedAt(LocalDateTime.now());
        return dayOffRepository.save(dayOff);
    }

    private void returnTheRemainingTime(DayOff dayOff, DayOffAccount dayOffAccount) {
        dayOffAccount.setRemainingTime(dayOffAccount.getRemainingTime() + dayOff.getNumberOfHours());
        dayOffAccountRepository.save(dayOffAccount);
    }

    private DayOff checkIfRequestIsAnswered(long dayOffId)
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

}

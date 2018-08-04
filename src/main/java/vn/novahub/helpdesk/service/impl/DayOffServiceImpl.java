package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.enums.DayOffEnum;
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.*;
import vn.novahub.helpdesk.repository.CommonDayOffTypeRepository;
import vn.novahub.helpdesk.repository.DayOffRepository;
import vn.novahub.helpdesk.repository.DayOffTypeRepository;
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
    private DayOffTypeRepository dayOffTypeRepository;

    @Autowired
    private CommonDayOffTypeRepository commonDayOffTypeRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private DayOffTypeService dayOffTypeService;

    @Autowired
    private Environment env;

    @Autowired
    private MailService mailService;

    @Override
    public DayOff add(DayOff dayOff)
            throws MessagingException,
            CommonTypeIsNotExistException,
            DayOffTypeIsExistException,
            IOException {

        initialize(dayOff);

        DayOff newDayOff = dayOffRepository.save(dayOff);

        sendEmailDayOff(dayOff, true);

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
    public Page<DayOff> getAllByStatusAndKeyword(String status, String keyword, Pageable pageable) {
        if (status.equals("NON-CANCELLED")) {
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
        if (dayOffOptional.get().getAccountId() != account.getId()
                && !account.getRole().getName().equals("ADMIN")) {
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

        DayOff dayOff = checkIfRequestIsAnswered(dayOffId, token);

        if (dayOff.getToken().equals(token)) {
            dayOff.getDayOffType().subtractRemainingTime(dayOff.getNumberOfHours());
            answerDayOffRequest(dayOff, DayOffEnum.APPROVED.name());
        } else {
            throw new DayOffTokenIsNotMatchException();
        }

        sendEmailDayOff(dayOff, false);

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
        DayOff dayOff = checkIfRequestIsAnswered(dayOffId, token);

        if (dayOff.getToken().equals(token)) {
            answerDayOffRequest(dayOff, DayOffEnum.DENIED.name());
        } else {
            throw new DayOffTokenIsNotMatchException();
        }

        sendEmailDayOff(dayOff, false);

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
        Account account = accountService.getAccountLogin();

        if (!dayOffOptional.isPresent()) {
            throw new DayOffIsNotExistException(dayOffId);
        }

        if (dayOffOptional.get().getAccountId() != account.getId()
                && !account.getRole().getName().equals("ADMIN")) {
            throw new AccountNotFoundException("Account is not admin or not own this day off");
        }

        if (dayOffOptional.get().getStatus().equals("CANCELLED")) {
            throw new DayOffIsAnsweredException(dayOffId);
        }

        if (LocalDate.now().isAfter(dayOffOptional.get().getStartDate())) {
            throw new DayOffOverdueException(dayOffId);
        }

        returnTheRemainingTime(dayOffOptional.get(), dayOffOptional.get().getDayOffType());

        return answerDayOffRequest(dayOffOptional.get(), "CANCELLED");
    }


    private void initialize(DayOff dayOff) throws CommonTypeIsNotExistException, DayOffTypeIsExistException {

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

    private DayOffType updateOrCreateDayOffType(CommonDayOffType commonDayOffType, int year)
            throws DayOffTypeIsExistException,
            CommonTypeIsNotExistException {
        Account accountLogin = accountService.getAccountLogin();
        DayOffType dayOffType =  dayOffTypeRepository
                .findByAccountIdAndCommonTypeIdAndYear(
                        accountLogin.getId(),
                        commonDayOffType.getId(),
                        year);
        if (dayOffType == null) {
            dayOffType = new DayOffType();
            dayOffType.setCommonTypeId(commonDayOffType.getId());
            dayOffType.setAccountId(accountLogin.getId());
            return dayOffTypeService.add(dayOffType);
        }
        return dayOffType;
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

    private void sendEmailDayOff(DayOff dayOff, boolean isSentToAdmin)
            throws MessagingException, IOException {
        Account accountLogin = accountService.getAccountLogin();
        Mail mail = new Mail();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy");
        String dayOffMailName = env.getProperty("day_off_mail_name");
        String hostUrl = env.getProperty("host_url");
        String[] emailList;

        String subject = "Day off request of " + accountLogin.getEmail() + " has been " + dayOff.getStatus();
        mail.setSubject(subject);
        String content = mailService.getContentMail(dayOffMailName);
        if (!isSentToAdmin) {
            content = content.replace("{email}", "Admin");
            emailList = new String[]{dayOff.getDayOffType().getAccount().getEmail()};
        } else {
            content = content.replace("{email}", accountLogin.getEmail());
            emailList = mailService.getEmailsOfAdminAndClerk().toArray(new String[0]);
        }
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

    private DayOff answerDayOffRequest(DayOff dayOff, String status) {
        dayOff.setToken("");
        dayOff.setStatus(status);
        dayOff.setUpdatedAt(LocalDateTime.now());
        return dayOffRepository.save(dayOff);
    }

    private void returnTheRemainingTime(DayOff dayOff, DayOffType dayOffType) {
        dayOffType.setRemainingTime(dayOffType.getRemainingTime() + dayOff.getNumberOfHours());
        dayOffTypeRepository.save(dayOffType);
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

}

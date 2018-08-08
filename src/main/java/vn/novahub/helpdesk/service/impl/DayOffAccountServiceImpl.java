package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.DayOffAccountIsExistException;
import vn.novahub.helpdesk.exception.DayOffAccountNotFoundException;
import vn.novahub.helpdesk.exception.DayOffTypeIsNotExistException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.DayOffAccount;
import vn.novahub.helpdesk.model.DayOffType;
import vn.novahub.helpdesk.repository.DayOffTypeRepository;
import vn.novahub.helpdesk.repository.DayOffAccountRepository;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.DayOffAccountService;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

@Service
public class DayOffAccountServiceImpl implements DayOffAccountService {

    @Autowired
    private DayOffAccountRepository dayOffAccountRepository;

    @Autowired
    private DayOffTypeRepository dayOffTypeRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public DayOffAccount add(DayOffAccount dayOffAccount)
            throws DayOffAccountIsExistException,
            DayOffTypeIsNotExistException {

        Account account = accountService.getAccountLogin();

        Optional<DayOffType> commonDayOffType =
                dayOffTypeRepository.findById(dayOffAccount.getDayOffTypeId());

        if (!commonDayOffType.isPresent()) {
            throw new DayOffTypeIsNotExistException();
        }

        dayOffAccount.setPrivateQuota(commonDayOffType.get().getDefaultQuota());
        dayOffAccount.setRemainingTime(dayOffAccount.getPrivateQuota());
        dayOffAccount.setYear(getCreatedYear(new Date()));
        dayOffAccount.setDayOffType(commonDayOffType.get());

        if (dayOffAccount.getAccountId() == 0) {
            dayOffAccount.setAccountId(account.getId());
        }

        DayOffAccount existingDayOffAccount =
                dayOffAccountRepository
                        .findByAccountIdAndDayOffTypeIdAndYear(
                                dayOffAccount.getAccountId(),
                                dayOffAccount.getDayOffTypeId(),
                                dayOffAccount.getYear());

        if (existingDayOffAccount != null) {
            throw new DayOffAccountIsExistException(dayOffAccount.getDayOffTypeId());
        }

        return dayOffAccountRepository.save(dayOffAccount);
    }

    @Override
    public DayOffAccount update(DayOffAccount dayOffAccount) throws DayOffAccountNotFoundException {
        Optional<DayOffAccount> currentDayOffType = dayOffAccountRepository.findById(dayOffAccount.getId());
        if (!currentDayOffType.isPresent()) {
            throw new DayOffAccountNotFoundException(dayOffAccount.getId());
        }

        if (dayOffAccount.getRemainingTime() == 0) {
            int newRemainingTime = currentDayOffType.get().getRemainingTime()
                    + dayOffAccount.getPrivateQuota() - currentDayOffType.get().getPrivateQuota();
            currentDayOffType.get().setRemainingTime(newRemainingTime);
        } else {
            currentDayOffType.get().setRemainingTime(dayOffAccount.getRemainingTime());
        }

        currentDayOffType.get().setPrivateQuota(dayOffAccount.getPrivateQuota());

        return dayOffAccountRepository.save(currentDayOffType.get());
    }

    @Override
    public Page<DayOffAccount> getAll(Pageable pageable) {
        return dayOffAccountRepository.findAll(pageable);
    }

    @Override
    public DayOffAccount getById(long id) throws DayOffAccountNotFoundException {
        Optional<DayOffAccount> dayOffAccountOptional = dayOffAccountRepository.findById(id);

        if (!dayOffAccountOptional.isPresent()) {
            throw new DayOffAccountNotFoundException(id);
        }

        return dayOffAccountOptional.get();
    }

    @Override
    public Page<DayOffAccount> findByAccountId(long accountId, Pageable pageable) {
        return dayOffAccountRepository.findAllByAccountId(accountId, pageable);
    }

   private int getCreatedYear(Date createdDate) {
       Calendar calendar = new GregorianCalendar();
       calendar.setTime(createdDate);
       return calendar.get(Calendar.YEAR);
  }
}

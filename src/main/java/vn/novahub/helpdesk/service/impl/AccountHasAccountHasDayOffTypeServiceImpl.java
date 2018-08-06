package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.AccountHasDayOffTypeIsExistException;
import vn.novahub.helpdesk.exception.AccountHasDayOffTypeNotFoundException;
import vn.novahub.helpdesk.exception.DayOffTypeIsNotExistException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.AccountHasDayOffType;
import vn.novahub.helpdesk.model.DayOffType;
import vn.novahub.helpdesk.repository.DayOffTypeRepository;
import vn.novahub.helpdesk.repository.AccountHasDayOffTypeRepository;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.AccountHasDayOffTypeService;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

@Service
public class AccountHasAccountHasDayOffTypeServiceImpl implements AccountHasDayOffTypeService {

    @Autowired
    private AccountHasDayOffTypeRepository accountHasDayOffTypeRepository;

    @Autowired
    private DayOffTypeRepository dayOffTypeRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public AccountHasDayOffType add(AccountHasDayOffType accountHasDayOffType)
            throws AccountHasDayOffTypeIsExistException,
            DayOffTypeIsNotExistException {

        Account account = accountService.getAccountLogin();

        Optional<DayOffType> commonDayOffType =
                dayOffTypeRepository.findById(accountHasDayOffType.getDayOffTypeId());

        if (!commonDayOffType.isPresent()) {
            throw new DayOffTypeIsNotExistException();
        }

        accountHasDayOffType.setPrivateQuota(commonDayOffType.get().getDefaultQuota());
        accountHasDayOffType.setRemainingTime(accountHasDayOffType.getPrivateQuota());
        accountHasDayOffType.setYear(getCreatedYear(new Date()));
        accountHasDayOffType.setDayOffType(commonDayOffType.get());

        if (accountHasDayOffType.getAccountId() == 0) {
            accountHasDayOffType.setAccountId(account.getId());
        }

        AccountHasDayOffType existingAccountHasDayOffType =
                accountHasDayOffTypeRepository
                        .findByAccountIdAndCommonTypeIdAndYear(
                                accountHasDayOffType.getAccountId(),
                                accountHasDayOffType.getDayOffTypeId(),
                                accountHasDayOffType.getYear());

        if (existingAccountHasDayOffType != null) {
            throw new AccountHasDayOffTypeIsExistException(accountHasDayOffType.getDayOffTypeId());
        }

        return accountHasDayOffTypeRepository.save(accountHasDayOffType);
    }

    @Override
    public AccountHasDayOffType update(AccountHasDayOffType accountHasDayOffType) throws AccountHasDayOffTypeNotFoundException {
        Optional<AccountHasDayOffType> currentDayOffType = accountHasDayOffTypeRepository.findById(accountHasDayOffType.getId());
        if (!currentDayOffType.isPresent()) {
            throw new AccountHasDayOffTypeNotFoundException(accountHasDayOffType.getId());
        }

        if (accountHasDayOffType.getRemainingTime() == 0) {
            int newRemainingTime = currentDayOffType.get().getRemainingTime()
                    + accountHasDayOffType.getPrivateQuota() - currentDayOffType.get().getPrivateQuota();
            currentDayOffType.get().setRemainingTime(newRemainingTime);
        } else {
            currentDayOffType.get().setRemainingTime(accountHasDayOffType.getRemainingTime());
        }

        currentDayOffType.get().setPrivateQuota(accountHasDayOffType.getPrivateQuota());

        return accountHasDayOffTypeRepository.save(currentDayOffType.get());
    }

    @Override
    public Page<AccountHasDayOffType> getAll(Pageable pageable) {
        return accountHasDayOffTypeRepository.findAll(pageable);
    }


    @Override
    public Page<AccountHasDayOffType> findByAccountId(long accountId, Pageable pageable) {
        return accountHasDayOffTypeRepository.findAllByAccountId(accountId, pageable);
    }

   private int getCreatedYear(Date createdDate) {
       Calendar calendar = new GregorianCalendar();
       calendar.setTime(createdDate);
       return calendar.get(Calendar.YEAR);
  }
}

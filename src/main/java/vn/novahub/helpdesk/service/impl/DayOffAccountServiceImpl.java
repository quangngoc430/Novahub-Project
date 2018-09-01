package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.DayOffAccountIsExistException;
import vn.novahub.helpdesk.exception.DayOffAccountNotFoundException;
import vn.novahub.helpdesk.exception.DayOffTypeNotFoundException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.DayOffAccount;
import vn.novahub.helpdesk.model.DayOffType;
import vn.novahub.helpdesk.repository.DayOffTypeRepository;
import vn.novahub.helpdesk.repository.DayOffAccountRepository;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.DayOffAccountService;

import java.util.*;

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
            DayOffTypeNotFoundException {

        Account account = accountService.getAccountLogin();

        Optional<DayOffType> dayOffType =
                dayOffTypeRepository.findById(dayOffAccount.getDayOffTypeId());

        if (!dayOffType.isPresent()) {
            throw new DayOffTypeNotFoundException();
        }

        dayOffAccount.setPrivateQuota(dayOffType.get().getDefaultQuota());
        dayOffAccount.setRemainingTime(dayOffAccount.getPrivateQuota());
        dayOffAccount.setYear(getCreatedYear(new Date()));
        dayOffAccount.setDayOffType(dayOffType.get());

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
    public Page<DayOffAccount> findByAccountId(long accountId, Pageable pageable)
            throws DayOffAccountIsExistException,
                    DayOffTypeNotFoundException {
        if (!isAccountHasAllDayOffAccount(accountId)) {
            generateDayOffAccountIfNotExist(accountId);
        }
        return dayOffAccountRepository.findAllByAccountId(accountId, pageable);
    }

    private void generateDayOffAccountIfNotExist(long accountId)
            throws DayOffAccountIsExistException,
                   DayOffTypeNotFoundException {

        List<DayOffType> dayOffTypes = dayOffTypeRepository.findAll();

        for (DayOffType dayOffType : dayOffTypes) {
            DayOffAccount dayOffAccount =
                    dayOffAccountRepository
                            .findByAccountIdAndDayOffTypeIdAndYear(
                                    accountId,
                                    dayOffType.getId(),
                                    getCreatedYear(new Date()));
            if (dayOffAccount == null) {
                dayOffAccount = new DayOffAccount();
                dayOffAccount.setDayOffTypeId(dayOffType.getId());
                dayOffAccount.setAccountId(accountId);
                dayOffAccount.setDayOffType(dayOffType);
                add(dayOffAccount);
            }
        }
    }

    private boolean isAccountHasAllDayOffAccount(long accountId) {
        List<DayOffAccount> dayOffAccounts =
                dayOffAccountRepository
                        .findAllByAccountIdAndYear(accountId, getCreatedYear(new Date()));

        return dayOffAccounts.size() == dayOffTypeRepository.findAll().size();
    }

    private int getCreatedYear(Date createdDate) {
       Calendar calendar = new GregorianCalendar();
       calendar.setTime(createdDate);
       return calendar.get(Calendar.YEAR);
   }
}

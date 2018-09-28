package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.dayoffaccount.DayOffAccountIsExistException;
import vn.novahub.helpdesk.exception.dayoffaccount.DayOffAccountNotFoundException;
import vn.novahub.helpdesk.exception.dayofftype.DayOffTypeNotFoundException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.DayOff;
import vn.novahub.helpdesk.model.DayOffAccount;
import vn.novahub.helpdesk.model.DayOffType;
import vn.novahub.helpdesk.repository.AccountRepository;
import vn.novahub.helpdesk.repository.DayOffRepository;
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
    private DayOffRepository dayOffRepository;

    @Autowired
    private AccountRepository accountRepository;

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
        dayOffAccount.setYear(getYearOfDate(new Date()));
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
    public void generateAllDayOffAccount(int year) throws DayOffAccountIsExistException, DayOffTypeNotFoundException {
        List<DayOffAccount> dayOffAccounts = dayOffAccountRepository.findAllByYear(year);
        List<Account> accounts = (List) accountRepository.findAll();
        if (dayOffAccounts.size() == accounts.size()) {
            throw new DayOffAccountIsExistException("DayOffAccount list in " + year + " have already generated");
        }

        for (Account account : accounts) {
            generateDayOffAccountIfNotExist(account.getId(), year);
            modifyYearlyQuota(account, year);
        }
    }

    @Override
    public void deleteAllDayOffAccount(int year) throws DayOffAccountNotFoundException {
        List<DayOffAccount> dayOffAccounts = dayOffAccountRepository.findAllByYear(year);
        List<DayOff> dayOffs = dayOffRepository.findByYear(year);
        if (dayOffAccounts.isEmpty()) {
            throw new DayOffAccountNotFoundException("DayOffAccount list is not exist");
        }

        if (!dayOffs.isEmpty()) {
            throw new DayOffAccountNotFoundException("Can not delete DayOffAccount list because it contains some DayOff request");
        }

        dayOffAccountRepository.deleteAll(dayOffAccounts);
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
        if (!isAccountHasAllDayOffAccount(accountId, getYearOfDate(new Date()))) {
            generateDayOffAccountIfNotExist(accountId, getYearOfDate(new Date()));
        }
        return dayOffAccountRepository.findAllByAccountId(accountId, pageable);
    }

    private void generateDayOffAccountIfNotExist(long accountId, int year) {

        List<DayOffType> dayOffTypes = dayOffTypeRepository.findAll();

        for (DayOffType dayOffType : dayOffTypes) {
            DayOffAccount dayOffAccount =
                    dayOffAccountRepository
                            .findByAccountIdAndDayOffTypeIdAndYear(
                                    accountId,
                                    dayOffType.getId(),
                                    year);
            Optional<Account> account = accountRepository.findById(accountId);
            if (dayOffAccount == null) {
                dayOffAccount = new DayOffAccount();
                dayOffAccount.setDayOffTypeId(dayOffType.getId());
                dayOffAccount.setAccountId(accountId);
                dayOffAccount.setDayOffType(dayOffType);
                if(account.isPresent()) {
                    dayOffAccount.setAccount(account.get());
                }
                dayOffAccount.setPrivateQuota(dayOffType.getDefaultQuota());
                dayOffAccount.setRemainingTime(dayOffAccount.getPrivateQuota());
                dayOffAccount.setYear(year);

                dayOffAccountRepository.save(dayOffAccount);
            }
        }
    }

    private boolean isAccountHasAllDayOffAccount(long accountId, int year) {
        List<DayOffAccount> dayOffAccounts =
                dayOffAccountRepository
                        .findAllByAccountIdAndYear(accountId, year);

        return dayOffAccounts.size() == dayOffTypeRepository.findAll().size();
    }

    //This method is writen according to Company Policy
    //TODO: Yearly is hardcode, may be change it later
    private void modifyYearlyQuota(Account account, int year) {
        int joiningYear = getYearOfDate(account.getJoiningDate());
        int joiningMonth = getMonthOfDate(account.getJoiningDate());
        DayOffType dayOffType = dayOffTypeRepository.findByType("Yearly");
        DayOffAccount dayOffAccount =
                dayOffAccountRepository
                        .findByAccountIdAndDayOffTypeIdAndYear(
                                account.getId(),
                                dayOffType.getId(),
                                year);
        if (year == joiningYear) {
            return;
        }
        int modifiedPrivateQuota;
        if (joiningMonth > Calendar.SEPTEMBER) {
            modifiedPrivateQuota = dayOffAccount.getPrivateQuota() + 8 * (year - joiningYear - 1);
        } else {
            modifiedPrivateQuota = dayOffAccount.getPrivateQuota() + 8 * (year - joiningYear);
        }

        dayOffAccount.setPrivateQuota(modifiedPrivateQuota);

        dayOffAccountRepository.save(dayOffAccount);
    }

    private int getYearOfDate(Date date) {
       Calendar calendar = new GregorianCalendar();
       calendar.setTime(date);
       return calendar.get(Calendar.YEAR);
   }

    private int getMonthOfDate(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }
}

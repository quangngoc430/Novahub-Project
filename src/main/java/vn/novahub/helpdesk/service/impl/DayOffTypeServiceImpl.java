package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.CommonTypeIsNotExistException;
import vn.novahub.helpdesk.exception.DayOffTypeIsExistException;
import vn.novahub.helpdesk.exception.DayOffTypeNotFoundException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.CommonDayOffType;
import vn.novahub.helpdesk.model.DayOffType;
import vn.novahub.helpdesk.repository.CommonDayOffTypeRepository;
import vn.novahub.helpdesk.repository.DayOffTypeRepository;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.DayOffTypeService;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

@Service
public class DayOffTypeServiceImpl implements DayOffTypeService {

    @Autowired
    private DayOffTypeRepository dayOffTypeRepository;

    @Autowired
    private CommonDayOffTypeRepository commonDayOffTypeRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public DayOffType add(DayOffType dayOffType)
            throws DayOffTypeIsExistException,
                    CommonTypeIsNotExistException {

        Account account = accountService.getAccountLogin();

        Optional<CommonDayOffType> commonDayOffType =
                commonDayOffTypeRepository.findById(dayOffType.getCommonTypeId());

        if (!commonDayOffType.isPresent()) {
            throw new CommonTypeIsNotExistException();
        }

        dayOffType.setPrivateQuota(commonDayOffType.get().getQuota());
        dayOffType.setRemainingTime(dayOffType.getPrivateQuota());
        dayOffType.setYear(getCreatedYear(new Date()));
        dayOffType.setCommonDayOffType(commonDayOffType.get());

        if (dayOffType.getAccountId() == 0) {
            dayOffType.setAccountId(account.getId());
        }

        DayOffType existingDayOffType =
                dayOffTypeRepository
                        .findByAccountIdAndCommonTypeIdAndYear(
                                dayOffType.getAccountId(),
                                dayOffType.getCommonTypeId(),
                                dayOffType.getYear());

        if (existingDayOffType != null) {
            throw new DayOffTypeIsExistException(dayOffType.getCommonTypeId());
        }

        return dayOffTypeRepository.save(dayOffType);
    }

    @Override
    public DayOffType update(DayOffType dayOffType) throws DayOffTypeNotFoundException {
        Optional<DayOffType> currentDayOffType = dayOffTypeRepository.findById(dayOffType.getId());
        if (!currentDayOffType.isPresent()) {
            throw new DayOffTypeNotFoundException(dayOffType.getId());
        }

        if (dayOffType.getRemainingTime() == 0) {
            int newRemainingTime = currentDayOffType.get().getRemainingTime()
                    + dayOffType.getPrivateQuota() - currentDayOffType.get().getPrivateQuota();
            currentDayOffType.get().setRemainingTime(newRemainingTime);
        } else {
            currentDayOffType.get().setRemainingTime(dayOffType.getRemainingTime());
        }

        currentDayOffType.get().setPrivateQuota(dayOffType.getPrivateQuota());

        return dayOffTypeRepository.save(currentDayOffType.get());
    }

    @Override
    public void delete(DayOffType dayOffType) {

    }

    @Override
    public Page<DayOffType> getAll(Pageable pageable) {
        return dayOffTypeRepository.findAll(pageable);
    }


    @Override
    public Page<DayOffType> findByAccountId(long accountId, Pageable pageable) {
        return dayOffTypeRepository.findByAccountId(accountId, pageable);
    }

   private int getCreatedYear(Date createdDate) {
       Calendar calendar = new GregorianCalendar();
       calendar.setTime(createdDate);
       return calendar.get(Calendar.YEAR);
  }
}

package vn.novahub.helpdesk.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.constant.DayOffConstant;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.DayOff;
import vn.novahub.helpdesk.model.DayOffType;
import vn.novahub.helpdesk.repository.DayOffRepository;
import vn.novahub.helpdesk.repository.DayOffTypeRepository;

import java.util.Date;
import java.util.Random;

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

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void add(DayOff dayOff) {
        Account accountLogin = accountService.getAccountLogin();

        dayOff.setCreatedAt(new Date());
        dayOff.setUpdatedAt(new Date());
        dayOff.setStatus(DayOffConstant.STATUS_PENDING);
        dayOff.setToken(tokenService.generateToken(accountLogin.getId() + dayOff.getTitle()));
        dayOff.setAccountId(accountLogin.getId());

        setDayOffType(dayOff);

        subtractRemainingTime(dayOff);

        dayOffRepository.save(dayOff);
    }

    private void setDayOffType(DayOff dayOff) {
        DayOffType dayOffType = dayOffTypeRepository
                .findByAccountIdAndType(
                        dayOff.getAccountId(),
                        dayOff.getType());
        dayOff.setDayOffType(dayOffType);
    }

    private void subtractRemainingTime(DayOff dayOff) {
        dayOff.getDayOffType().subtractRemainingTime(dayOff.getNumberOfHours());
    }


}

package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.AccountHasDayOffTypeNotFoundException;
import vn.novahub.helpdesk.exception.DayOffTypeIsNotExistException;
import vn.novahub.helpdesk.exception.AccountHasDayOffTypeIsExistException;
import vn.novahub.helpdesk.model.AccountHasDayOffType;

public interface AccountHasDayOffTypeService {

    AccountHasDayOffType add(AccountHasDayOffType accountHasDayOffType)
            throws AccountHasDayOffTypeIsExistException, DayOffTypeIsNotExistException;

    Page<AccountHasDayOffType> findByAccountId(long accountId, Pageable pageable);

    Page<AccountHasDayOffType> getAll(Pageable pageable);

    AccountHasDayOffType update(AccountHasDayOffType accountHasDayOffType) throws AccountHasDayOffTypeNotFoundException;

}

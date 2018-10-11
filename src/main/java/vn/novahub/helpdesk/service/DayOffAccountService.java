package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.dayoffaccount.DayOffAccountNotFoundException;
import vn.novahub.helpdesk.exception.dayofftype.DayOffTypeNotFoundException;
import vn.novahub.helpdesk.exception.dayoffaccount.DayOffAccountIsExistException;
import vn.novahub.helpdesk.model.DayOffAccount;

public interface DayOffAccountService {

    DayOffAccount add(DayOffAccount dayOffAccount)
            throws DayOffAccountIsExistException, DayOffTypeNotFoundException;

    void generateAllDayOffAccount(int year) throws DayOffAccountIsExistException, DayOffTypeNotFoundException;

    void deleteAllDayOffAccount(int year) throws DayOffAccountNotFoundException;

    Page<DayOffAccount> findByAccountId(long accountId, Pageable pageable) throws DayOffAccountIsExistException, DayOffTypeNotFoundException;

    Page<DayOffAccount> findByAccountIdAndYear(long accountId, int year, Pageable pageable) throws DayOffAccountNotFoundException;

    Page<DayOffAccount> getAll(Pageable pageable);

    DayOffAccount getById(long id) throws DayOffAccountNotFoundException;

    DayOffAccount update(DayOffAccount dayOffAccount) throws DayOffAccountNotFoundException;

}

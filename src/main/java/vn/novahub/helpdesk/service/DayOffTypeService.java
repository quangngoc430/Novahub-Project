package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.model.DayOffType;

public interface DayOffTypeService {

    void addNewDayOffType(DayOffType dayOffType);

    void modifyQuota(DayOffType dayOffType);

    void deleteDayOffType(DayOffType dayOffType);

    DayOffType findByIdAndAccountId(long typeId, long accountId);

    Page<DayOffType> findByAccountId(long accountId, Pageable pageable);
}

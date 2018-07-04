package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import vn.novahub.helpdesk.model.DayOffType;

public interface DayOffTypeService {

    void addNewDayOffType(DayOffType dayOffType);

    void modifyQuota(DayOffType dayOffType, int quota);

    void deleteDayOffType(DayOffType dayOffType);

    DayOffType findByAccountIdAndType(long accountId, String type);

    Page<DayOffType> findByAccountId(long accountId);
}

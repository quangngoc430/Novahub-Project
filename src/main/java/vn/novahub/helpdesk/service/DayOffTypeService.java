package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.CommonTypeIsNotExistException;
import vn.novahub.helpdesk.exception.DayOffTypeIsExistException;
import vn.novahub.helpdesk.exception.DayOffTypeNotFoundException;
import vn.novahub.helpdesk.model.DayOffType;

public interface DayOffTypeService {

    DayOffType add(DayOffType dayOffType)
            throws DayOffTypeIsExistException, CommonTypeIsNotExistException;

    Page<DayOffType> findByAccountId(long accountId, Pageable pageable);

    Page<DayOffType> getAll(Pageable pageable);

    DayOffType update(DayOffType dayOffType) throws DayOffTypeNotFoundException;

}

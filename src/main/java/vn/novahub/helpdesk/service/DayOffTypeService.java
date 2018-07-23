package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.DayOffTypeIsExistException;
import vn.novahub.helpdesk.exception.DayOffTypeIsNotValidException;
import vn.novahub.helpdesk.exception.DayOffTypeNotFoundException;
import vn.novahub.helpdesk.model.DayOffType;

public interface DayOffTypeService {

    DayOffType add(DayOffType dayOffType) throws DayOffTypeIsExistException, DayOffTypeIsNotValidException;

    void update(DayOffType dayOffType) throws DayOffTypeNotFoundException;

    void delete(DayOffType dayOffType);

    DayOffType getById(long typeId) throws DayOffTypeNotFoundException;

    Page<DayOffType> findByAccountId(long accountId, Pageable pageable);
}

package vn.novahub.helpdesk.service;

import vn.novahub.helpdesk.exception.dayofftype.DayOffTypeExistException;
import vn.novahub.helpdesk.exception.dayofftype.DayOffTypeNotFoundException;
import vn.novahub.helpdesk.model.DayOffType;

import java.util.List;

public interface DayOffTypeService {

    List<DayOffType> getAllDayOffType();

    DayOffType getById(int id) throws DayOffTypeNotFoundException;

    DayOffType create(DayOffType dayOffType) throws DayOffTypeExistException;

    DayOffType update(DayOffType dayOffType) throws DayOffTypeNotFoundException;

    DayOffType delete(int id) throws DayOffTypeNotFoundException, DayOffTypeExistException;
}

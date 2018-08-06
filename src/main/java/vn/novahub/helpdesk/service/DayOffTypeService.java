package vn.novahub.helpdesk.service;

import vn.novahub.helpdesk.exception.DayOffTypeIsNotExistException;
import vn.novahub.helpdesk.model.DayOffType;

import java.util.List;

public interface DayOffTypeService {

    List<DayOffType> getAllDayOffType();

    DayOffType getById(int id) throws DayOffTypeIsNotExistException;

    DayOffType create(DayOffType dayOffType);

    DayOffType update(DayOffType dayOffType) throws DayOffTypeIsNotExistException;

    DayOffType delete(int id) throws DayOffTypeIsNotExistException;
}

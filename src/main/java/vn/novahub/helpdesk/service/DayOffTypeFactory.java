package vn.novahub.helpdesk.service;

import vn.novahub.helpdesk.exception.DayOffTypeIsNotValidException;
import vn.novahub.helpdesk.model.DayOffType;

public interface DayOffTypeFactory {
    boolean isValidDayOffType(String type);

    DayOffType create(String type) throws DayOffTypeIsNotValidException;
}

package vn.novahub.helpdesk.service;

import vn.novahub.helpdesk.exception.DayOffTypeIsNotValidException;
import vn.novahub.helpdesk.model.DayOff;

import javax.mail.MessagingException;

public interface DayOffService {

    void add(DayOff dayOff) throws MessagingException, DayOffTypeIsNotValidException;

    void approve(DayOff dayOff);
}

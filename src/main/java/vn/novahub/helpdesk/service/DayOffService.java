package vn.novahub.helpdesk.service;

import vn.novahub.helpdesk.exception.DayOffIsAnsweredException;
import vn.novahub.helpdesk.exception.DayOffTokenIsNotMatchException;
import vn.novahub.helpdesk.exception.DayOffTypeIsNotValidException;
import vn.novahub.helpdesk.model.DayOff;

import javax.mail.MessagingException;

public interface DayOffService {

    void add(DayOff dayOff)
            throws MessagingException,
            DayOffTypeIsNotValidException;

    void update(DayOff dayOff)
            throws MessagingException;

    void approve(long dayOffId, String token)
            throws DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            MessagingException;

    void deny(long dayOffId, String token)
            throws DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            MessagingException;
}

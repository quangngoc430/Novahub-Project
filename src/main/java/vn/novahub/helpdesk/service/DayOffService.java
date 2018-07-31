package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.DayOff;

import javax.mail.MessagingException;

public interface DayOffService {

    DayOff add(DayOff dayOff)
            throws MessagingException,
            DayOffTypeIsNotValidException;

    Page<DayOff> getAllByAccountIdAndTypeAndStatus(
            long accountId,
            String type,
            String status,
            Pageable pageable);

    DayOff delete(long dayOffId)
            throws MessagingException,
                   DayOffOverdueException,
                   DayOffIsNotExistException,
                   UnauthorizedException;

    DayOff approve(long dayOffId, String token)
            throws DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            DayOffIsNotExistException,
            MessagingException;

    DayOff deny(long dayOffId, String token)
            throws DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            DayOffIsNotExistException,
            MessagingException;
}

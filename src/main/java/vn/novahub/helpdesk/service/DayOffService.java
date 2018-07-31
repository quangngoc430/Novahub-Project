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

    void delete(DayOff dayOff)
            throws MessagingException,
            DayOffOverdueException, AccountNotFoundException;

    void approve(long dayOffId, String token)
            throws DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            MessagingException, AccountNotFoundException;

    void deny(long dayOffId, String token)
            throws DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            MessagingException, AccountNotFoundException;
}

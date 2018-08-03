package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.DayOff;

import javax.mail.MessagingException;
import java.io.IOException;

public interface DayOffService {

    DayOff add(DayOff dayOff)
            throws MessagingException,
            CommonTypeIsNotExistException,
            IOException;

    Page<DayOff> getAllByAccountIdAndStatus(
            long accountId,
            String status,
            Pageable pageable);

    DayOff getById(long id) throws DayOffIsNotExistException;


    DayOff delete(long dayOffId)
            throws MessagingException,
                   DayOffOverdueException,
                   DayOffIsNotExistException,
                   UnauthorizedException,
                   AccountNotFoundException;


    DayOff approve(long dayOffId, String token)
            throws DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            DayOffIsNotExistException,
            MessagingException,
            AccountNotFoundException,
            IOException ;


    DayOff deny(long dayOffId, String token)
            throws DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            DayOffIsNotExistException,
            MessagingException,
            AccountNotFoundException,
            IOException;

}

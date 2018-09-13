package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.exception.dayoff.DayOffIsAnsweredException;
import vn.novahub.helpdesk.exception.dayoff.DayOffIsNotExistException;
import vn.novahub.helpdesk.exception.dayoff.DayOffOverdueException;
import vn.novahub.helpdesk.exception.dayoff.DayOffTokenIsNotMatchException;
import vn.novahub.helpdesk.exception.dayoffaccount.DayOffAccountIsExistException;
import vn.novahub.helpdesk.exception.dayofftype.DayOffTypeNotFoundException;
import vn.novahub.helpdesk.model.DayOff;

import javax.mail.MessagingException;
import java.io.IOException;

public interface DayOffService {

    DayOff add(DayOff dayOff)
            throws MessagingException,
            DayOffTypeNotFoundException,
            DayOffAccountIsExistException,
            AccountNotFoundException,
            IOException;

    Page<DayOff> getAllByAccountIdAndStatus(
            long accountId,
            String status,
            Pageable pageable);

    Page<DayOff> getAllByStatus(
            String status,
            Pageable pageable);

    DayOff getById(long id)
            throws DayOffIsNotExistException,
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

    DayOff approve(long dayOffId)
            throws DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            DayOffIsNotExistException,
            MessagingException,
            AccountNotFoundException,
            IOException ;


    DayOff deny(long dayOffId)
            throws DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            DayOffIsNotExistException,
            MessagingException,
            AccountNotFoundException,
            IOException;

    DayOff cancel(long dayOffId)
            throws DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            DayOffIsNotExistException,
            MessagingException,
            AccountNotFoundException,
            DayOffOverdueException,
            IOException;

}

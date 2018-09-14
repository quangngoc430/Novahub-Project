package vn.novahub.helpdesk.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.enums.DayOffStatus;
import vn.novahub.helpdesk.exception.AccountNotFoundException;
import vn.novahub.helpdesk.exception.dayoff.DayOffIsAnsweredException;
import vn.novahub.helpdesk.exception.dayoff.DayOffNotFoundException;
import vn.novahub.helpdesk.exception.dayoff.DayOffOverdueException;
import vn.novahub.helpdesk.exception.dayoff.DayOffTokenIsNotMatchException;
import vn.novahub.helpdesk.model.DayOff;
import vn.novahub.helpdesk.service.DayOffService;
import vn.novahub.helpdesk.view.View;

import javax.annotation.security.PermitAll;
import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping(path = "/api/admin/day-offs", produces = {MediaType.APPLICATION_JSON_VALUE})
public class DayOffAdminController {

    @Autowired
    private DayOffService dayOffService;

    @PermitAll
    @JsonView(View.DayOffRespond.class)
    @GetMapping("/{id}/answer-token")
    public ResponseEntity<DayOff> answerWithToken(@PathVariable("id") long dayOffId,
                                          @RequestParam("status") String status,
                                          @RequestParam(name = "token") String token)
                                             throws DayOffIsAnsweredException,
                                                    DayOffTokenIsNotMatchException,
                                                    DayOffNotFoundException,
                                                    MessagingException,
                                                    AccountNotFoundException,
                                                    IOException {
        DayOff dayOff;
        DayOffStatus dayOffStatus;
        try {
            dayOffStatus = DayOffStatus.valueOf(status);
        } catch (Exception e) {
            throw new DayOffNotFoundException("The parameter \'status\' is not valid");
        }

        switch (dayOffStatus) {
            case APPROVED:
                dayOff = dayOffService.approve(dayOffId, token);
                break;
            case DENIED:
                dayOff = dayOffService.deny(dayOffId, token);
               break;
            default:
                throw new DayOffNotFoundException("The parameter \'status\' is not valid");
        }
        return new ResponseEntity<>(dayOff, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @JsonView(View.DayOffRespond.class)
    @GetMapping("/{id}/answer")
    public ResponseEntity<DayOff> answerWithoutToken(@PathVariable("id") long dayOffId,
                                                  @RequestParam("status") String status)
            throws DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            DayOffNotFoundException,
            MessagingException,
            AccountNotFoundException,
            DayOffOverdueException,
            IOException {

        DayOff dayOff;
        DayOffStatus dayOffStatus;

        try {
            dayOffStatus = DayOffStatus.valueOf(status);
        } catch (Exception e) {
            throw new DayOffNotFoundException("The parameter \'status\' is not valid");
        }

        switch (dayOffStatus) {
            case APPROVED:
                dayOff = dayOffService.approve(dayOffId);
                break;
            case DENIED:
                dayOff = dayOffService.deny(dayOffId);
                break;
            case CANCELLED:
                dayOff = dayOffService.cancel(dayOffId);
                break;
            default:
                throw new DayOffNotFoundException("The parameter \'status\' is not valid");
        }
        return new ResponseEntity<>(dayOff, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @JsonView(View.DayOffRespond.class)
    @GetMapping
    public ResponseEntity<Page<DayOff>> getAllDayOffs(
            @RequestParam(name = "accountId", required = false, defaultValue = "") String accountIdString,
            @RequestParam(name = "status", required = false, defaultValue = "") String status,
            Pageable pageable) {

        long accountId = 0;
        if (!accountIdString.equals("")) {
            accountId = Long.parseLong(accountIdString);
        }

        if (accountId == 0) {
            return new ResponseEntity<>(dayOffService.getAllByStatus(status, pageable),
            HttpStatus.OK);
        }

        return new ResponseEntity<>(
                dayOffService.getAllByAccountIdAndStatus(accountId, status, pageable),
                HttpStatus.OK);
    }
}

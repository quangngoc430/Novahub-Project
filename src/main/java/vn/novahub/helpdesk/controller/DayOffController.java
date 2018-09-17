package vn.novahub.helpdesk.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.apache.http.impl.execchain.RequestAbortedException;
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
import vn.novahub.helpdesk.exception.UnauthorizedException;
import vn.novahub.helpdesk.exception.dayoff.DayOffIsAnsweredException;
import vn.novahub.helpdesk.exception.dayoff.DayOffNotFoundException;
import vn.novahub.helpdesk.exception.dayoff.DayOffOverdueException;
import vn.novahub.helpdesk.exception.dayoff.DayOffTokenIsNotMatchException;
import vn.novahub.helpdesk.exception.dayoffaccount.DayOffAccountIsExistException;
import vn.novahub.helpdesk.exception.dayofftype.DayOffTypeNotFoundException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.DayOff;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.DayOffService;
import vn.novahub.helpdesk.view.View;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping(path = "/api/day-offs", produces = {MediaType.APPLICATION_JSON_VALUE})
public class DayOffController {

    @Autowired
    private DayOffService dayOffService;

    @Autowired
    private AccountService accountService;

    @PreAuthorize("isAuthenticated()")
    @JsonView(View.DayOffRespond.class)
    @GetMapping
    public ResponseEntity<Page<DayOff>> getUserLoginDayOffs(@RequestParam(name = "status", required = false, defaultValue = "") String status,
                                                           Pageable pageable) throws RequestAbortedException {
        Account account = accountService.getAccountLogin();
        return new ResponseEntity<>(
                dayOffService.getAllByAccountIdAndStatus(account.getId(), status, pageable),
                HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @JsonView(View.DayOffRespond.class)
    @GetMapping("/{id}")
    public ResponseEntity<DayOff> getById(@PathVariable("id") long id)
                                               throws DayOffNotFoundException, AccountNotFoundException {
        DayOff dayOff = dayOffService.getById(id);
        return new ResponseEntity<>(dayOff, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @JsonView(View.DayOffRespond.class)
    @PostMapping
    public ResponseEntity<DayOff> create(@RequestBody DayOff dayOff)
            throws MessagingException,
            IOException,
            DayOffAccountIsExistException,
            DayOffTypeNotFoundException,
            AccountNotFoundException {

        DayOff newDayOff = dayOffService.add(dayOff);

        return new ResponseEntity<>(newDayOff, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @JsonView(View.DayOffRespond.class)
    @DeleteMapping("/{id}")
    public ResponseEntity<DayOff> cancel(@PathVariable("id") long id)
            throws MessagingException,
            IOException,
            AccountNotFoundException,
            DayOffTokenIsNotMatchException,
            DayOffOverdueException,
            DayOffNotFoundException,
            DayOffIsAnsweredException,
            UnauthorizedException {

        DayOff dayOff = dayOffService.getById(id);

        if (!dayOff.getStatus().equals(DayOffStatus.PENDING.name())) {
            throw new UnauthorizedException("User can not cancelled the answered day offs ");
        }

        return new ResponseEntity<>(dayOffService.cancel(id), HttpStatus.OK);
    }
}

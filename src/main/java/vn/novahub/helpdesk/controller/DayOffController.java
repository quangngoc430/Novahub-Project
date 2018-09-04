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
import vn.novahub.helpdesk.exception.*;
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
                                               throws DayOffIsNotExistException, AccountNotFoundException {
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

}

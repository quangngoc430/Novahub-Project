package vn.novahub.helpdesk.controller;

import org.apache.http.impl.execchain.RequestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.enums.DayOffEnum;
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.DayOff;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.DayOffService;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class DayOffController {

    @Autowired
    private DayOffService dayOffService;

    @Autowired
    private AccountService accountService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/day-offs")
    public ResponseEntity<Page<DayOff>> getUserLoginDayOffs(@RequestParam(name = "status", required = false, defaultValue = "") String status,
                                                           Pageable pageable) {
        Account account = accountService.getAccountLogin();
        return new ResponseEntity<>(
                dayOffService.getAllByAccountIdAndStatus(account.getId(), status,pageable),
                HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/day-offs/{id}")
    public ResponseEntity<DayOff> getById(@PathVariable("id") long id)
                                               throws DayOffIsNotExistException, AccountNotFoundException {
        DayOff dayOff = dayOffService.getById(id);
        return new ResponseEntity<>(dayOff, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "/day-offs")
    public ResponseEntity<DayOff> create(@RequestBody DayOff dayOff)
            throws MessagingException,
            IOException,
            DayOffAccountIsExistException,
            DayOffTypeIsNotExistException,
            AccountNotFoundException {

        dayOff = dayOffService.add(dayOff);

        return new ResponseEntity<>(dayOff, HttpStatus.OK);
    }



    @GetMapping(path = "/day-offs/{id}/answer")
    public ResponseEntity<DayOff> answer(@PathVariable("id") long dayOffId,
                                          @RequestParam("status") String status,
                                          @RequestParam(name = "token") String token)
                                             throws DayOffIsAnsweredException,
                                                    DayOffTokenIsNotMatchException,
                                                    DayOffIsNotExistException,
                                                    MessagingException,
                                                    AccountNotFoundException,
                                                    IOException {
        DayOff dayOff;
        if (status.equals(DayOffEnum.APPROVED.name())) {
            dayOff = dayOffService.approve(dayOffId, token);
        } else if (status.equals(DayOffEnum.DENIED.name())) {
            dayOff = dayOffService.deny(dayOffId, token);
        } else {
            throw new RequestAbortedException("The parameter \'status\' is not valid");
        }
        return new ResponseEntity<>(dayOff, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/day-offs/{id}/cancel")
    public ResponseEntity<DayOff> cancel(@PathVariable("id") long dayOffId)
                                            throws DayOffIsAnsweredException,
                                            DayOffTokenIsNotMatchException,
                                            DayOffIsNotExistException,
                                            MessagingException,
                                            AccountNotFoundException,
                                            DayOffOverdueException,
                                            IOException {

        return new ResponseEntity<>(dayOffService.cancel(dayOffId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/admin/day-offs")
    public ResponseEntity<Page<DayOff>> getAllDayOffs(
            @RequestParam(name = "status", required = false, defaultValue = "") String status,
            @RequestParam(name= "keyword", required = false, defaultValue = "") String keyword,
            Pageable pageable) {
        return new ResponseEntity<>(
                dayOffService.getAllByStatusAndKeyword(status, keyword ,pageable),
                HttpStatus.OK);
    }

}

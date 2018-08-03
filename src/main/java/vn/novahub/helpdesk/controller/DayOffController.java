package vn.novahub.helpdesk.controller;

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
    public ResponseEntity<Page<DayOff>> getUserLoginDayOff(@RequestParam(name = "status", required = false, defaultValue = "") String status,
                                                           Pageable pageable) {
        Account account = accountService.getAccountLogin();
        return new ResponseEntity<>(
                dayOffService.getAllByAccountIdAndStatus(account.getId(), status,pageable),
                HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/day-offs/{id}")
    public ResponseEntity<DayOff> getById(@PathVariable("id") long id)
                                               throws DayOffIsNotExistException {
        return new ResponseEntity<>(
                dayOffService.getById(id),
                HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "/day-offs")
    public ResponseEntity<DayOff> create(@RequestBody DayOff dayOff)
            throws MessagingException,
            IOException,
            CommonTypeIsNotExistException {

        dayOff = dayOffService.add(dayOff);

        return new ResponseEntity<>(dayOff, HttpStatus.OK);
    }



    @GetMapping(path = "/day-offs/{id}/approve")
    public ResponseEntity<DayOff> approve(@PathVariable("id") long dayOffId,
                                          @RequestParam("token") String token)
                                             throws DayOffIsAnsweredException,
                                                    DayOffTokenIsNotMatchException,
                                                    DayOffIsNotExistException,
                                                    MessagingException,
                                                    AccountNotFoundException,
                                                    IOException {
        DayOff dayOff = dayOffService.approve(dayOffId, token);
        return new ResponseEntity<>(dayOff, HttpStatus.OK);
    }

    @GetMapping(path = "/day-offs/{id}/deny")
    public ResponseEntity<DayOff> deny(@PathVariable("id") long dayOffId,
                                       @RequestParam("token") String token)
                                            throws DayOffIsAnsweredException,
                                                   DayOffTokenIsNotMatchException,
                                                   DayOffIsNotExistException,
                                                   MessagingException,
                                                   AccountNotFoundException,
                                                   IOException {
       DayOff dayOff =  dayOffService.deny(dayOffId, token);
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

        DayOff dayOff =  dayOffService.cancel(dayOffId);
        return new ResponseEntity<>(dayOff, HttpStatus.OK);
    }




//
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @GetMapping(path = "/day-offs", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<Page<DayOff>> getAllAccountIdAndTypeAndStatus(
//            @RequestParam(name = "account-id", required = false, defaultValue = "") String accountIdString,
//            @RequestParam(name = "type", required = false, defaultValue = "") String type,
//            @RequestParam(name = "status", required = false, defaultValue = "") String status,
//            Pageable pageable) {
//
//        long accountId;
//
//        if (accountIdString.equals("")) {
//            accountId = 0;
//        } else {
//            accountId = Long.parseLong(accountIdString);
//        }
//
//        Page<DayOff> dayOffPage = dayOffService.getAllByAccountIdAndTypeAndStatus(accountId, type, status, pageable);
//
//        return new ResponseEntity<>(dayOffPage, HttpStatus.OK);
//    }
//

//
//
//    @PreAuthorize("isAuthenticated()")
//    @DeleteMapping(path = "/day-offs/{id}")
//    public ResponseEntity<DayOff> delete(@PathVariable long dayOffId)
//            throws MessagingException,
//            DayOffOverdueException,
//            DayOffIsNotExistException,
//            UnauthorizedException,
//            AccountNotFoundException {
//
//        DayOff dayOff = dayOffService.delete(dayOffId);
//
//        return new ResponseEntity<>(dayOff, HttpStatus.OK);
//    }
//
//

}

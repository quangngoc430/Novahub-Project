package vn.novahub.helpdesk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.DayOff;
import vn.novahub.helpdesk.service.DayOffService;

import javax.mail.MessagingException;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class DayOffController {

    @Autowired
    private DayOffService dayOffService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/day-offs", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<DayOff>> getAllAccountIdAndTypeAndStatus(
            @RequestParam(name = "account-id", required = false, defaultValue = "") String accountIdString,
            @RequestParam(name = "type", required = false, defaultValue = "") String type,
            @RequestParam(name = "status", required = false, defaultValue = "") String status,
            Pageable pageable) {

        long accountId;

        if (accountIdString.equals("")) {
            accountId = 0;
        } else {
            accountId = Long.parseLong(accountIdString);
        }

        Page<DayOff> dayOffPage = dayOffService.getAllByAccountIdAndTypeAndStatus(accountId, type, status, pageable);

        return new ResponseEntity<>(dayOffPage, HttpStatus.OK);
    }


    @PostMapping(path = "/day-offs")
    public ResponseEntity<DayOff> create(@RequestBody DayOff dayOff)
            throws MessagingException, DayOffTypeIsNotValidException {

        dayOff = dayOffService.add(dayOff);

        return new ResponseEntity<>(dayOff, HttpStatus.OK);
    }

    @DeleteMapping(path = "/day-offs")
    public ResponseEntity<String> delete(@RequestBody DayOff dayOff)
            throws MessagingException, DayOffOverdueException{

        dayOffService.delete(dayOff);

        return new ResponseEntity<>("Deleting day off request successful", HttpStatus.OK);
    }

    @GetMapping(path = "/day-offs/{id}/approve")
    public ResponseEntity<String> approve(@PathVariable("id") long dayOffId,
                                          @RequestParam("token") String token)
                                             throws DayOffIsAnsweredException,
                                                    DayOffTokenIsNotMatchException,
                                                    MessagingException{
        dayOffService.approve(dayOffId, token);

        return new ResponseEntity<>("The day off request with id = " + dayOffId + " has been approved",
                                    HttpStatus.OK);
    }

    @GetMapping(path = "/day-offs/{id}/deny")
    public ResponseEntity<String> deny(@PathVariable("id") long dayOffId,
                                       @RequestParam("token") String token)
                                            throws DayOffIsAnsweredException,
                                                   DayOffTokenIsNotMatchException,
                                                   MessagingException {
        dayOffService.deny(dayOffId, token);

        return new ResponseEntity<>("The day off request with id = " + dayOffId + " has been denied",
                HttpStatus.OK);
    }


}

package vn.novahub.helpdesk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.DayOff;
import vn.novahub.helpdesk.model.DayOffType;
import vn.novahub.helpdesk.service.DayOffService;
import vn.novahub.helpdesk.service.DayOffTypeService;
import vn.novahub.helpdesk.service.LogService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class DayOffController {

    private static final Logger logger = LoggerFactory.getLogger(DayOffController.class);

    @Autowired
    private LogService logService;

    @Autowired
    private DayOffService dayOffService;

    @PostMapping(path = "/day-offs")
    public ResponseEntity<String> create(@RequestBody DayOff dayOff)
            throws MessagingException, DayOffTypeIsNotValidException{

        dayOffService.add(dayOff);

        return new ResponseEntity<>("Creating new day off request successful", HttpStatus.OK);
    }

    @PutMapping(path = "/day-offs")
    public ResponseEntity<String> update(@RequestBody DayOff dayOff)
            throws MessagingException {

        dayOffService.update(dayOff);

        return new ResponseEntity<>("Updating day off request successful", HttpStatus.OK);
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

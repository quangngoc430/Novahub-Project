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
import vn.novahub.helpdesk.repository.DayOffRepository;
import vn.novahub.helpdesk.service.DayOffService;
import vn.novahub.helpdesk.service.LogService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class DayOffController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LogService logService;

    @Autowired
    private DayOffService dayOffService;

    @GetMapping(path = "day-offs")
    public ResponseEntity<Page<DayOff>> getAll(Pageable pageable) {
        return new ResponseEntity<>(dayOffService.getAll(pageable), HttpStatus.OK);
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

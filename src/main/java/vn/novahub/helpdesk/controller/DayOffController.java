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
import vn.novahub.helpdesk.exception.DayOffTypeIsExistException;
import vn.novahub.helpdesk.exception.DayOffTypeIsNotValidException;
import vn.novahub.helpdesk.exception.DayOffTypeNotFoundException;
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
    public ResponseEntity<String> create(@RequestBody DayOff dayOff,
                                         HttpServletRequest request)
            throws MessagingException, DayOffTypeIsNotValidException{
        logService.log(request, logger);

        dayOffService.add(dayOff);

        return new ResponseEntity<>("Creating new day off request successful", HttpStatus.OK);
    }
}

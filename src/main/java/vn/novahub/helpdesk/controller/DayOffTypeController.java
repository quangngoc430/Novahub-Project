package vn.novahub.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.exception.CommonTypeIsNotExistException;
import vn.novahub.helpdesk.exception.DayOffIsNotExistException;
import vn.novahub.helpdesk.exception.DayOffTypeIsExistException;
import vn.novahub.helpdesk.exception.DayOffTypeNotFoundException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.DayOffType;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.DayOffTypeService;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class DayOffTypeController {

    @Autowired
    private DayOffTypeService dayOffTypeService;

    @Autowired
    private AccountService accountService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/day-off-types")
    public ResponseEntity<Page<DayOffType>> getUserLoginDayOffType(Pageable pageable) {
        Account account = accountService.getAccountLogin();

        return new ResponseEntity<>(
                dayOffTypeService.findByAccountId(account.getId(), pageable),
                HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/admin/day-off-types")
    public ResponseEntity<DayOffType> create(@RequestBody DayOffType dayOffType)
            throws DayOffTypeIsExistException, CommonTypeIsNotExistException {

        return new ResponseEntity<>(dayOffTypeService.add(dayOffType), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/admin/day-off-types")
    public ResponseEntity<Page<DayOffType>> getAllDayOffType(
         @RequestParam(name = "accountId", required = false, defaultValue = "0") String accountIdString,
         Pageable pageable) {

        long accountId = Long.parseLong(accountIdString);

        if (accountId != 0) {
            return new ResponseEntity<>(
                    dayOffTypeService.findByAccountId(accountId, pageable),
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(
                dayOffTypeService.getAll(pageable),
                HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/admin/day-off-types")
                                public ResponseEntity<DayOffType> update(@RequestBody DayOffType dayOffType)
                                                      throws DayOffTypeNotFoundException {

        dayOffType = dayOffTypeService.update(dayOffType);

        return new ResponseEntity<>(dayOffType, HttpStatus.OK);
    }


}

package vn.novahub.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.exception.AccountHasDayOffTypeIsExistException;
import vn.novahub.helpdesk.exception.DayOffTypeIsNotExistException;
import vn.novahub.helpdesk.exception.AccountHasDayOffTypeNotFoundException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.AccountHasDayOffType;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.AccountHasDayOffTypeService;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AccountHasDayOffTypeController {

    @Autowired
    private AccountHasDayOffTypeService accountHasDayOffTypeService;

    @Autowired
    private AccountService accountService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/account-has-day-off-types")
    public ResponseEntity<Page<AccountHasDayOffType>> userGet(Pageable pageable) {
        Account account = accountService.getAccountLogin();

        return new ResponseEntity<>(
                accountHasDayOffTypeService.findByAccountId(account.getId(), pageable),
                HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/admin/account-has-day-off-types")
    public ResponseEntity<AccountHasDayOffType> create(@RequestBody AccountHasDayOffType accountHasDayOffType)
            throws AccountHasDayOffTypeIsExistException, DayOffTypeIsNotExistException {

        return new ResponseEntity<>(accountHasDayOffTypeService.add(accountHasDayOffType), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/admin/account-has-day-off-types")
    public ResponseEntity<Page<AccountHasDayOffType>> adminGet(
         @RequestParam(name = "accountId", required = false, defaultValue = "0") String accountIdString,
         Pageable pageable) {

        long accountId = Long.parseLong(accountIdString);

        if (accountId != 0) {
            return new ResponseEntity<>(
                    accountHasDayOffTypeService.findByAccountId(accountId, pageable),
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(
                accountHasDayOffTypeService.getAll(pageable),
                HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/admin/account-has-day-off-types")
                                public ResponseEntity<AccountHasDayOffType> update(@RequestBody AccountHasDayOffType accountHasDayOffType)
                                                      throws AccountHasDayOffTypeNotFoundException {

        accountHasDayOffType = accountHasDayOffTypeService.update(accountHasDayOffType);

        return new ResponseEntity<>(accountHasDayOffType, HttpStatus.OK);
    }


}

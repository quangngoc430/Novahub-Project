package vn.novahub.helpdesk.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.exception.dayoffaccount.DayOffAccountIsExistException;
import vn.novahub.helpdesk.exception.dayoffaccount.DayOffAccountNotFoundException;
import vn.novahub.helpdesk.exception.dayofftype.DayOffTypeNotFoundException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.DayOffAccount;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.DayOffAccountService;
import vn.novahub.helpdesk.view.View;

@RestController
@RequestMapping(path = "/api/day-off-accounts", produces = {MediaType.APPLICATION_JSON_VALUE})
public class DayOffAccountController {

    @Autowired
    private DayOffAccountService dayOffAccountService;

    @Autowired
    private AccountService accountService;

    @PreAuthorize("isAuthenticated()")
    @JsonView(View.DayOffAccountRespond.class)
    @GetMapping
    public ResponseEntity<Page<DayOffAccount>> userGet(
            @RequestParam(name = "year", required = false, defaultValue = "") String yearString,
            Pageable pageable)
            throws DayOffAccountIsExistException,
                    DayOffTypeNotFoundException,
                    DayOffAccountNotFoundException {
        Account account = accountService.getAccountLogin();
        Page<DayOffAccount> dayOffAccounts;
        if (yearString.equals("")) {
            dayOffAccounts = dayOffAccountService.findByAccountId(account.getId(), pageable);
        } else {
            int year = Integer.parseInt(yearString);
            dayOffAccounts = dayOffAccountService.findByAccountIdAndYear(account.getId(), year, pageable);
        }

        return new ResponseEntity<>(dayOffAccounts, HttpStatus.OK);
    }
}

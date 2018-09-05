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
import vn.novahub.helpdesk.exception.DayOffAccountIsExistException;
import vn.novahub.helpdesk.exception.DayOffTypeNotFoundException;
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
    public ResponseEntity<Page<DayOffAccount>> userGet(Pageable pageable)
            throws DayOffAccountIsExistException,
                   DayOffTypeNotFoundException {

        Account account = accountService.getAccountLogin();
        return new ResponseEntity<>(
                dayOffAccountService.findByAccountId(account.getId(), pageable),
                HttpStatus.OK);
    }
}

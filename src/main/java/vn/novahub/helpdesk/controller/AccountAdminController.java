package vn.novahub.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.exception.AccountNotFoundException;
import vn.novahub.helpdesk.exception.AccountValidationException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.service.AccountService;

@RestController
@RequestMapping(path = "/api/admin")
public class AccountAdminController {

    @Autowired
    private AccountService accountService;

    @PreAuthorize("(hasRole('ROLE_ADMIN') and (@accountServiceImpl.isAccountLogin(#accountId) == false))")
    @PutMapping(path = "/users/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Account> updateForAdmin(@PathVariable("id") long accountId,
                                                  @RequestBody Account account) throws AccountValidationException, AccountNotFoundException {

        Account accountUpdated = accountService.updatedForAdmin(accountId, account);

        return new ResponseEntity<>(accountUpdated, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/users/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> delete(@PathVariable(value = "id") long accountId) throws AccountNotFoundException {
        accountService.delete(accountId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

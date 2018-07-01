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
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.LogService;

import javax.annotation.security.PermitAll;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private LogService logService;

    @PreAuthorize("hasRole('ROLE_ANONYMOUS')")
    @PostMapping(path = "/login", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Account> login(@RequestBody Account account,
                                      HttpServletRequest request) throws AccountInactiveException, AccountInvalidException, AccountLockedException, AccountValidationException {
        logService.log(request, logger);

        Account accountLogin = accountService.login(account, request);

        return new ResponseEntity<>(accountLogin, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/users", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Account>> getAll(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                HttpServletRequest request,
                                                Pageable pageable){
        logService.log(request, logger);

        Page<Account> accounts = accountService.getAll(keyword, pageable);

        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/users/me", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Account> getAccountLogin(HttpServletRequest request){
        logService.log(request, logger);

        Account account = accountService.getAccountLogin();

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(path = "/users/me", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Account> updateForAccountLogin(@RequestBody Account account,
                                                         HttpServletRequest request) throws AccountPasswordNotEqualException, AccountValidationException {
        logService.log(request, logger);

        Account accountUpdated = accountService.update(account);

        return new ResponseEntity<>(accountUpdated, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/users/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Account> get(@PathVariable(value = "id") long accountId,
                                       HttpServletRequest request) throws AccountNotFoundException {
        logService.log(request, logger);

        Account account = accountService.get(accountId);

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PermitAll
    @GetMapping(path = "/users/{id}/active", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> activate(@PathVariable(value = "id") long accountId,
                                          @RequestParam(value = "token", defaultValue = "") String token,
                                          HttpServletRequest request){
        logService.log(request, logger);

        boolean result = accountService.activateAccount(accountId, token);

        if(!result)
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PermitAll
    @PostMapping(path = "/users", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Account> create(@RequestBody Account account,
                                          HttpServletRequest request) throws AccountIsExistException, RoleNotFoundException, AccountValidationException, MessagingException {
        logService.log(request, logger);

        Account newAccount = accountService.create(account);

        return new ResponseEntity<>(newAccount, HttpStatus.OK);
    }

    @PreAuthorize("(hasRole('ROLE_ADMIN') and (@accountServiceImpl.isAccountLogin(#accountId) == false))")
    @PutMapping(path = "/users/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Account> updateForAdmin(@PathVariable("id") long accountId,
                                                  @RequestBody Account account,
                                                  HttpServletRequest request) throws AccountValidationException {
        logService.log(request, logger);

        Account accountUpdated = accountService.updatedForAdmin(accountId, account);

        return new ResponseEntity<>(accountUpdated, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/users/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> delete(@PathVariable(value = "id") long accountId,
                                       HttpServletRequest request) throws AccountNotFoundException {
        logService.log(request, logger);

        accountService.delete(accountId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

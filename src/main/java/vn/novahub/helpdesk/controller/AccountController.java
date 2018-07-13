package vn.novahub.helpdesk.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

import javax.annotation.security.PermitAll;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping(path = "/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PreAuthorize("hasRole('ROLE_ANONYMOUS')")
    @PostMapping(path = "/login", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Account> login(@RequestBody Account account,
                                         HttpServletRequest request) throws AccountInvalidException, AccountLockedException, AccountValidationException, AccountInactiveException {
        Account accountLogin = accountService.login(account, request);

        return new ResponseEntity<>(accountLogin, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/users", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Account>> getAll(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                @RequestParam(value = "status", required = false, defaultValue = "") String status,
                                                @RequestParam(value = "role", required = false, defaultValue = "") String role,
                                                Pageable pageable){
        Page<Account> accounts = accountService.getAll(keyword, status, role, pageable);

        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/users/me", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<JsonNode> getAccountLogin(@RequestParam(value = "checkPasswordNull", defaultValue = "false") String checkPasswordNull){
        Account account = accountService.getAccountLogin();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.valueToTree(account);

        if(checkPasswordNull.equals("true")){
            root.put("isPasswordNull", (account.getPassword() == null));
        }

        return new ResponseEntity<>(root, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(path = "/users/me", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<JsonNode> updateForAccountLogin(@RequestParam(value = "checkPasswordNull", defaultValue = "false") String checkPasswordNull,
                                                          @RequestBody Account account) throws AccountPasswordNotEqualException, AccountValidationException {
        Account accountUpdated = accountService.update(account);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.valueToTree(accountUpdated);

        if(checkPasswordNull.equals("true")){
            root.put("isPasswordNull", (account.getPassword() == null));
        }

        return new ResponseEntity<>(root, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/users/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Account> get(@PathVariable(value = "id") long accountId) throws AccountNotFoundException {
        Account account = accountService.get(accountId);

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PermitAll
    @GetMapping(path = "/users/{id}/active", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> activate(@PathVariable(value = "id") long accountId,
                                         @RequestParam(value = "token", defaultValue = "") String verficationToken){
        boolean result = accountService.activateAccount(accountId, verficationToken);

        if(!result)
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PermitAll
    @PostMapping(path = "/users", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Account> create(@RequestBody Account account) throws AccountIsExistException, RoleNotFoundException, AccountValidationException, MessagingException, IOException {
        Account newAccount = accountService.create(account);

        return new ResponseEntity<>(newAccount, HttpStatus.OK);
    }

    @PreAuthorize("(hasRole('ROLE_ADMIN') and (@accountServiceImpl.isAccountLogin(#accountId) == false))")
    @PutMapping(path = "/users/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Account> updateForAdmin(@PathVariable("id") long accountId,
                                                  @RequestBody Account account) throws AccountValidationException {
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

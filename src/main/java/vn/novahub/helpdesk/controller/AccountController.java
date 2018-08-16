package vn.novahub.helpdesk.controller;

import com.fasterxml.jackson.annotation.JsonView;
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
import vn.novahub.helpdesk.model.Token;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.AccountSkillService;
import vn.novahub.helpdesk.view.View;

import javax.annotation.security.PermitAll;
import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@RestController
@RequestMapping(path = "/api")
public class AccountController {

    @Autowired
    private AccountService accountService;
    
    @Autowired
    private AccountSkillService accountSkillService;

    @GetMapping(path = "/authentication-token")
    public void authenticationToken(HttpServletRequest request,
                                                       HttpServletResponse response) throws ServletException, IOException, TokenIsExpiredException, UnauthorizedException {
        handleAuthenticationToken(request, response);
    }

    @PostMapping(path = "/authentication-token-post")
    public void authenticationTokenPost(HttpServletRequest request,
                                    HttpServletResponse response) throws ServletException, IOException, TokenIsExpiredException, UnauthorizedException {
        handleAuthenticationToken(request, response);
    }

    @PutMapping(path = "/authentication-token-put")
    public void authenticationTokenPut(HttpServletRequest request,
                                       HttpServletResponse response) throws UnauthorizedException, TokenIsExpiredException, ServletException, IOException {
        handleAuthenticationToken(request, response);
    }

    @DeleteMapping(path = "/authentication-token-delete")
    public void authenticationTokenDelete(HttpServletRequest request,
                                          HttpServletResponse response) throws UnauthorizedException, TokenIsExpiredException, ServletException, IOException {
        handleAuthenticationToken(request, response);
    }

    private void handleAuthenticationToken(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, UnauthorizedException, TokenIsExpiredException {
        String accessToken = request.getHeader("access_token");
        String urlRequest = (String) request.getAttribute("url_request");

        accountService.authenticationToken(accessToken, request);

        request.removeAttribute("url_request");

        RequestDispatcher requestDispatcher = request.getServletContext().getRequestDispatcher(urlRequest);
        requestDispatcher.forward(request, response);
    }

    @PermitAll
    @JsonView(View.Public.class)
    @PostMapping(path = "/login", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Token> login(@RequestBody Account account) throws AccountInvalidException, AccountLockedException, AccountValidationException, AccountInactiveException {

        Token accessToken = accountService.login(account);

        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }

    @PermitAll
    @PostMapping(path = "/login/google", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Token> login(@RequestBody Token token) throws EmailFormatException, RoleNotFoundException, IOException, UnauthorizedException, TokenIsExpiredException, AccountValidationException {

        Token accessToken = accountService.loginWithGoogle(token);

        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }

    @PermitAll
    @GetMapping(path = "/logout", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> logout(@RequestHeader(value = "access_token", defaultValue = "") String accessToken) throws TokenNotFoundException {

        accountService.logout(accessToken);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/users", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<JsonNode> getAll(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                @RequestParam(value = "status", required = false, defaultValue = "") String status,
                                                @RequestParam(value = "role", required = false, defaultValue = "") String role,
                                                Pageable pageable) throws IOException {
        Page<Account> accounts = accountService.getAll(keyword, status, role, pageable);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writerWithView(View.AccountWithSkills.class).writeValueAsString(accounts);

        return new ResponseEntity<>(objectMapper.readTree(json), HttpStatus.OK);
    }

    @PermitAll
    @JsonView(View.Public.class)
    @PostMapping(path = "/users", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Account> create(@RequestBody Account account) throws AccountIsExistException, RoleNotFoundException, AccountValidationException, MessagingException, IOException {
        Account newAccount = accountService.create(account);

        return new ResponseEntity<>(newAccount, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @JsonView(View.Public.class)
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
    @JsonView(View.Public.class)
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
    @JsonView(View.Public.class)
    @GetMapping(path = "/users/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Account> get(@PathVariable(value = "id") long accountId) throws AccountNotFoundException {
        Account account = accountService.get(accountId);

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PermitAll
    @GetMapping(path = "/users/{id}/active", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> activate(@PathVariable(value = "id") long accountId,
                                         @RequestParam(value = "token", defaultValue = "") String verificationToken) {
        boolean result = accountService.activateAccount(accountId, verificationToken);

        if (!result)
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/users/{id}/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Skill>> getAllByAccountId(@PathVariable("id") long accountId,
                                                         Pageable pageable) throws AccountNotFoundException {
        return new ResponseEntity<>(accountSkillService.getAllByAccountId(accountId, pageable), HttpStatus.OK);
    }
}

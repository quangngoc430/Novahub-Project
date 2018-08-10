package vn.novahub.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.enums.IssueEnum;
import vn.novahub.helpdesk.exception.AccountNotFoundException;
import vn.novahub.helpdesk.exception.IssueIsClosedException;
import vn.novahub.helpdesk.exception.IssueNotFoundException;
import vn.novahub.helpdesk.exception.IssueValidationException;
import vn.novahub.helpdesk.model.Issue;
import vn.novahub.helpdesk.service.*;

import javax.annotation.security.PermitAll;
import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping(path = "/api")
public class IssueController {

    @Autowired
    private AccountIssueService accountIssueService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/issues/me", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Issue>> getAll(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                                              @RequestParam(name = "status", required = false, defaultValue = "") String status,
                                              Pageable pageable){
        Page<Issue> issuePage = accountIssueService.getAllByKeywordAndStatus(keyword, status, pageable);

        return new ResponseEntity<>(issuePage, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "/issues/me", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Issue> create(@RequestBody Issue issue) throws IssueValidationException, MessagingException, IOException, AccountNotFoundException {
        issue = accountIssueService.create(issue);

        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/issues/me/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Issue> findOne(@PathVariable("issueId") long issueId) throws IssueNotFoundException {
        return new ResponseEntity<>(accountIssueService.findOne(issueId), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(path = "/issues/me/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Issue> update(@RequestBody Issue issue,
                                        @PathVariable(name = "issueId") long issueId) throws IssueNotFoundException, IssueValidationException, MessagingException, IOException, IssueIsClosedException, AccountNotFoundException {
        issue = accountIssueService.update(issueId, issue);

        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(path = "/issues/me/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> delete(@PathVariable(name = "issueId") long issueId) throws IssueNotFoundException {
        accountIssueService.delete(issueId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PermitAll
    @GetMapping(path = "/issues/{issueId}/action", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> action(@RequestParam(name = "status", required = false, defaultValue = "") String status,
                                       @RequestParam(name = "token", required = false, defaultValue = "") String token,
                                       @PathVariable(name = "issueId") long issueId) throws IssueNotFoundException, IssueIsClosedException, MessagingException, IOException {
        if(status.equals(IssueEnum.APPROVE.name())) {
            accountIssueService.approve(issueId, token);
        } else if(status.equals(IssueEnum.DENY.name())){
            accountIssueService.deny(issueId, token);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

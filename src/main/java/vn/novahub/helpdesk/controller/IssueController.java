package vn.novahub.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.exception.IssueIsClosedException;
import vn.novahub.helpdesk.exception.IssueNotFoundException;
import vn.novahub.helpdesk.exception.IssueValidationException;
import vn.novahub.helpdesk.model.Issue;
import vn.novahub.helpdesk.service.IssueService;
import vn.novahub.helpdesk.service.LogService;

import javax.annotation.security.PermitAll;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api")
public class IssueController {

    private static final Logger logger = LoggerFactory.getLogger(IssueController.class);

    @Autowired
    private IssueService issueService;

    @Autowired
    private LogService logService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/issues", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Issue>> getAllForAdmin(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                                                       @RequestParam(name = "status", required = false, defaultValue = "") String status,
                                                       Pageable pageable,
                                                       HttpServletRequest request){
        logService.log(request, logger);

        Page<Issue> issuePage = issueService.getAllByKeywordAndStatusForAdmin(keyword, status, pageable);

        return new ResponseEntity<>(issuePage, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Issue> getForAdmin(@PathVariable(name = "issueId") long issueId,
                                              HttpServletRequest request) throws IssueNotFoundException {
        logService.log(request, logger);
        Issue issue = issueService.getByIdForAdmin(issueId);

        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Issue> updateForAdmin(@PathVariable(name = "issueId") long issueId,
                                                @RequestBody Issue issue,
                                                HttpServletRequest request) throws IssueValidationException, IssueNotFoundException {
        logService.log(request, logger);
        Issue issueUpdated = issueService.updateForAdmin(issueId, issue);

        return new ResponseEntity<>(issueUpdated, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> deleteForAdmin(@PathVariable(name = "issueId") long issueId,
                                               HttpServletRequest request) throws IssueNotFoundException {
        logService.log(request, logger);
        issueService.deleteForAdmin(issueId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/users/me/issues", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Issue>> getAll(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                                                            @RequestParam(name = "status", required = false, defaultValue = "") String status,
                                                            HttpServletRequest request,
                                                            Pageable pageable){
        logService.log(request, logger);
        Page<Issue> issuePage = issueService.getAllByKeywordAndStatus(keyword, status, pageable);

        return new ResponseEntity<>(issuePage, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/users/me/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Issue> get(@PathVariable("issueId") long issueId,
                                                HttpServletRequest request) throws IssueNotFoundException {
        logService.log(request, logger);
        Issue issue = issueService.getById(issueId);

        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "/users/me/issues", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Issue> create(HttpServletRequest request,
                                                 @RequestBody Issue issue) throws IssueValidationException, MessagingException {
        logService.log(request, logger);
        issue = issueService.create(issue);

        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated() and @issueServiceImpl.isIssueOfAccountLogin(#issueId)")
    @PutMapping(path = "/users/me/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Issue> update(HttpServletRequest request,
                                                 @RequestBody Issue issue,
                                                 @PathVariable(name = "issueId") long issueId) throws IssueNotFoundException, IssueValidationException {
        logService.log(request, logger);
        issue = issueService.update(issueId, issue);

        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated() and @issueServiceImpl.isIssueOfAccountLogin(#issueId)")
    @DeleteMapping(path = "/users/me/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> delete(@PathVariable(name = "issueId") long issueId,
                                       HttpServletRequest request) throws IssueNotFoundException {
        logService.log(request, logger);
        issueService.delete(issueId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PermitAll
    @GetMapping(path = "/issues/{issueId}/approve", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> approve(@RequestParam(name = "token", required = false, defaultValue = "") String token,
                                        @PathVariable(name = "issueId") long issueId,
                                        HttpServletRequest request) throws IssueNotFoundException, IssueIsClosedException {
        logService.log(request, logger);
        issueService.approve(issueId, token);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PermitAll
    @GetMapping(path = "/issues/{issueId}/deny", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> deny(@RequestParam(name = "token", required = false, defaultValue = "") String token,
                                     @PathVariable(name = "issueId") long issueId,
                                     HttpServletRequest request) throws IssueNotFoundException, IssueIsClosedException {
        logService.log(request, logger);
        issueService.deny(issueId, token);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

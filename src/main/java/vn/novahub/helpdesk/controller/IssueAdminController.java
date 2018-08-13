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
import vn.novahub.helpdesk.exception.IssueIsClosedException;
import vn.novahub.helpdesk.exception.IssueNotFoundException;
import vn.novahub.helpdesk.exception.IssueValidationException;
import vn.novahub.helpdesk.model.Issue;
import vn.novahub.helpdesk.service.AdminIssueService;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping(path = "/api/admin/issues")
public class IssueAdminController {

    @Autowired
    private AdminIssueService adminIssueService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Issue>> getAllByAdmin(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                                                     @RequestParam(name = "status", required = false, defaultValue = "") String status,
                                                     Pageable pageable){
        Page<Issue> issuePage = adminIssueService.getAllByKeywordAndStatus(keyword, status, pageable);

        return new ResponseEntity<>(issuePage, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Issue> findOneByAdmin(@PathVariable(name = "issueId") long issueId) throws IssueNotFoundException {
        return new ResponseEntity<>(adminIssueService.findOne(issueId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Issue> updateForAdmin(@PathVariable(name = "issueId") long issueId,
                                                @RequestBody Issue issue) throws IssueValidationException, IssueNotFoundException, MessagingException, IOException {
        Issue issueUpdated = adminIssueService.update(issueId, issue);

        return new ResponseEntity<>(issueUpdated, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> deleteByAdmin(@PathVariable(name = "issueId") long issueId) throws IssueNotFoundException {
        adminIssueService.delete(issueId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/{issueId}/action", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> actionForAdmin(@RequestParam(name = "status", required = false, defaultValue = "") String status,
                                               @PathVariable(value = "issueId") long issueId) throws MessagingException, IOException, IssueNotFoundException, IssueIsClosedException {
        if(status.equals(IssueEnum.APPROVE.name())) {
            adminIssueService.approve(issueId);
        } else if (status.equals(IssueEnum.DENY.name())) {
            adminIssueService.deny(issueId);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

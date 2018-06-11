package vn.novahub.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.model.Issue;
import vn.novahub.helpdesk.service.IssueService;

@RestController
public class IssueController {

    @Autowired
    private IssueService issueService;

    @GetMapping(value = "/issues", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllIssues(@RequestParam(name = "keyword", required = false) String keyword,
                                          @RequestParam(name = "status", required = false) String status,
                                          Pageable pageable){
        Page<Issue> issues = issueService.getAllIssuesByKeyword(keyword, status, pageable);

        return new ResponseEntity<>(issues, HttpStatus.OK);
    }

    @GetMapping(value = "/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAnIssue(@PathVariable(name = "issueId") long issueId){
        //TODO check accountId is exist
        Issue issue = issueService.getIssueByIssueId(issueId);

        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @GetMapping(value = "/accounts/{accountId}/issues", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllIssuesOfAnAccount(@RequestParam(name = "keyword", required = false) String keyword,
                                                     @RequestParam(name = "status", required = false) String status,
                                                     @PathVariable(name = "accountId") long accountId,
                                                     Pageable pageable){
        //TODO check accountId is exist
        Page<Issue> issues = issueService.getAllIssuesOfAccountByKeyword(accountId, keyword, status, pageable);

        return new ResponseEntity<>(issues, HttpStatus.OK);
    }

    @GetMapping(value = "/accounts/{accountId}/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAnIssueOfAnAccount(@PathVariable(name = "accountId") long accountId,
                                                   @PathVariable(name = "issueId") long issueId){
        //TODO check accountId is exist
        //TODO check issueId is exist
        Issue issue = issueService.getIssueOfAccountByIssueIdAndAccountId(issueId, accountId);

        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @PostMapping(value = "/issues", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createAnIssue(@RequestBody Issue issue){
        //TODO check value of issue 
        issue = issueService.createIssue(issue);

        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @PutMapping(value = "/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateIssue(@RequestBody Issue issue,
                                         @PathVariable(name = "issueId") long issueId){
        //TODO check issueId is exist
        issue = issueService.updateIssue(issueId, issue);

        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @DeleteMapping(value = "/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteIssue(@PathVariable(name = "issueId") long issueId){
        // TODO check issueId is exist
        boolean isDeleted = issueService.deleteIssue(issueId);

        return new ResponseEntity<>(isDeleted, HttpStatus.OK);
    }
}

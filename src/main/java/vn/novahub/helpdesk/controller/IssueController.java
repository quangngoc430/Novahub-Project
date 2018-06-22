package vn.novahub.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.constant.ExceptionConstant;
import vn.novahub.helpdesk.constant.ResponseConstant;
import vn.novahub.helpdesk.exception.IssueNotFoundException;
import vn.novahub.helpdesk.model.Issue;
import vn.novahub.helpdesk.service.IssueService;
import vn.novahub.helpdesk.service.LogService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api")
public class IssueController {

    private static final Logger logger = LoggerFactory.getLogger(IssueController.class);

    @Autowired
    private IssueService issueService;

    @Autowired
    private LogService logService;

    @GetMapping(path = "/issues", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Issue>> getAllIssues(@RequestParam(name = "keyword", required = false) String keyword,
                                                       @RequestParam(name = "status", required = false) String status,
                                                       Pageable pageable,
                                                       HttpServletRequest request){
        logService.log(request, logger);
        Page<Issue> issuePage = issueService.getAllByKeyword(keyword, status, pageable, request);

        return new ResponseEntity<>(issuePage, HttpStatus.OK);
    }

    @GetMapping(path = "/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Issue> get(@PathVariable(name = "issueId") long issueId,
                                              HttpServletRequest request) throws IssueNotFoundException {
        logService.log(request, logger);
        Issue issue = issueService.getByIssueId(issueId, request);

        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @GetMapping(path = "/accounts/issues", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Issue>> getAllOfAnAccount(@RequestParam(name = "keyword", required = false) String keyword,
                                                            @RequestParam(name = "status", required = false) String status,
                                                            HttpServletRequest request,
                                                            Pageable pageable){
        logService.log(request, logger);
        Page<Issue> issuePage = issueService.getAllOfAccountByKeyword(keyword, status, pageable, request);

        return new ResponseEntity<>(issuePage, HttpStatus.OK);
    }

    @PostMapping(path = "/issues", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Issue> create(HttpServletRequest request,
                                                 @RequestBody Issue issue){
        logService.log(request, logger);
        issue = issueService.create(issue, request);

        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @PutMapping(path = "/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Issue> update(HttpServletRequest request,
                                                 @RequestBody Issue issue,
                                                 @PathVariable(name = "issueId") long issueId) throws IssueNotFoundException {
        logService.log(request, logger);
        issue = issueService.update(issueId, issue, request);

        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @DeleteMapping(path = "/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> delete(@PathVariable(name = "issueId") long issueId,
                                                 HttpServletRequest request) throws IssueNotFoundException {
        logService.log(request, logger);
        issueService.delete(issueId, request);

        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @GetMapping(path = "/issues/{issueId}/approve", produces = {MediaType.APPLICATION_JSON_VALUE})
    public void approve(@RequestParam(name = "token") String token,
                        @PathVariable(name = "issueId") long issueId,
                        HttpServletRequest request) throws IssueNotFoundException {
        logService.log(request, logger);
        issueService.approve(issueId, token, request);
    }

    @GetMapping(path = "/issues/{issueId}/deny", produces = {MediaType.APPLICATION_JSON_VALUE})
    public void deny(@RequestParam(name = "token") String token,
                     @PathVariable(name = "issueId") long issueId,
                     HttpServletRequest request) throws IssueNotFoundException {
        logService.log(request, logger);
        issueService.deny(issueId, token, request);
    }
}

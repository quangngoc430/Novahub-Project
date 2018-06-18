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
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Issue;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.IssueService;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;

@RestController
public class IssueController {

    private static final Logger logger = LoggerFactory.getLogger(IssueController.class);

    @Autowired
    private IssueService issueService;

    @Autowired
    private AccountService accountService;

    @GetMapping(value = "/admin/issues")
    public ModelAndView getAdminIssues(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("issues_admin");

        return modelAndView;
    }

    @GetMapping(value = "/api/issues", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllIssues(@RequestParam(name = "keyword", required = false) String keyword,
                                          @RequestParam(name = "status", required = false) String status,
                                          Pageable pageable){
        Page<Issue> issues = issueService.getAllIssuesByKeyword(keyword, status, pageable);

        return new ResponseEntity<>(issues, HttpStatus.OK);
    }

    @GetMapping(value = "/api/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAnIssue(@PathVariable(name = "issueId") long issueId,
                                        HttpServletRequest request){
        //TODO check accountId is exist

        logger.info("accountLogin : " + request.getSession().getAttribute("accountLogin").toString());

        Issue issue = issueService.getIssueByIssueId(issueId);

        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @GetMapping(value = "/api/accounts/issues", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllIssuesOfAnAccount(@RequestParam(name = "keyword", required = false) String keyword,
                                                     @RequestParam(name = "status", required = false) String status,
                                                     HttpServletRequest request,
                                                     Pageable pageable){
        Account accountLogin = accountService.getAccountLogin(request);

        Page<Issue> issues = issueService.getAllIssuesOfAccountByKeyword(accountLogin.getId(), keyword, status, pageable);

        return new ResponseEntity<>(issues, HttpStatus.OK);
    }

    @GetMapping(value = "/api/issues/{issueId}/approve", produces = {MediaType.APPLICATION_JSON_VALUE})
    public void approveIssue(@RequestParam(name = "token") String token,
                                          @PathVariable(name = "issueId") long issueId,
                                          HttpServletRequest request){
        System.out.println(issueService.approveIssue(issueId, token));
    }

    @GetMapping(value = "/api/issues/{issueId}/deny", produces = {MediaType.APPLICATION_JSON_VALUE})
    public void denyIssue(@RequestParam(name = "token") String token,
                             @PathVariable(name = "issueId") long issueId,
                             HttpServletRequest request){
        System.out.println(issueService.denyIssue(issueId, token));
    }


    @PostMapping(value = "/api/issues", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createAnIssue(HttpServletRequest request,
                                           @RequestBody Issue issue){
        logger.info("URL : " + request.getRequestURL());
        //TODO check value of issue 
        issue = issueService.createIssue(issue, request);

        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @PutMapping(value = "/api/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateIssue(HttpServletRequest request,
                                         @RequestBody Issue issue,
                                         @PathVariable(name = "issueId") long issueId){
        logger.info("URL : " + request.getRequestURL());
        //TODO check issueId is exist
        issue = issueService.updateIssue(issueId, issue, request);

        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteIssue(@PathVariable(name = "issueId") long issueId){
        // TODO check issueId is exist
        issueService.deleteIssue(issueId);

        return new ResponseEntity<>(isDeleted, HttpStatus.OK);
    }
}

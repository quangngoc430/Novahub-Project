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
import vn.novahub.helpdesk.model.ResponseJSON;
import vn.novahub.helpdesk.service.IssueService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api")
public class IssueController {

    private static final Logger logger = LoggerFactory.getLogger(IssueController.class);

    @Autowired
    private IssueService issueService;


    private static void log(HttpServletRequest request){
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("Method : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
    }

    @ExceptionHandler(IssueNotFoundException.class)
    public ResponseEntity<ResponseJSON> handleEmployeeNotFoundException(HttpServletRequest request, Exception ex){
        ResponseJSON responseJSON = new ResponseJSON();
        responseJSON.setCode(ExceptionConstant.CODE_ISSUE_IS_NOT_EXIST);
        responseJSON.setData(ExceptionConstant.MESSAGE_ISSUE_IS_NOT_EXIST);

        return new ResponseEntity<ResponseJSON>(responseJSON, HttpStatus.OK);
    }

    @GetMapping(path = "/issues", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseJSON> getAllIssues(@RequestParam(name = "keyword", required = false) String keyword,
                                          @RequestParam(name = "status", required = false) String status,
                                          Pageable pageable,
                                          HttpServletRequest request){
        log(request);
        Page<Issue> issuePage = issueService.getAllByKeyword(keyword, status, pageable, request);

        ResponseJSON responseJSON = new ResponseJSON();
        responseJSON.setCode(ResponseConstant.OK);
        responseJSON.setData(issuePage);

        return new ResponseEntity<ResponseJSON>(responseJSON, HttpStatus.OK);
    }

    @GetMapping(path = "/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseJSON> get(@PathVariable(name = "issueId") long issueId,
                                        HttpServletRequest request) throws IssueNotFoundException {
        log(request);
        Issue issue = issueService.getByIssueId(issueId, request);

        ResponseJSON responseJSON = new ResponseJSON();
        responseJSON.setCode(ResponseConstant.OK);
        responseJSON.setData(issue);

        return new ResponseEntity<ResponseJSON>(responseJSON, HttpStatus.OK);
    }

    @GetMapping(path = "/accounts/issues", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseJSON> getAllOfAnAccount(@RequestParam(name = "keyword", required = false) String keyword,
                                                     @RequestParam(name = "status", required = false) String status,
                                                     HttpServletRequest request,
                                                     Pageable pageable){
        log(request);
        Page<Issue> issuePage = issueService.getAllOfAccountByKeyword(keyword, status, pageable, request);

        ResponseJSON responseJSON = new ResponseJSON();
        responseJSON.setCode(ResponseConstant.OK);
        responseJSON.setData(issuePage);

        return new ResponseEntity<ResponseJSON>(responseJSON, HttpStatus.OK);
    }

    @PostMapping(path = "/issues", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseJSON> create(HttpServletRequest request,
                                           @RequestBody Issue issue){
        log(request);
        issue = issueService.create(issue, request);

        ResponseJSON responseJSON = new ResponseJSON();
        responseJSON.setCode(ResponseConstant.OK);
        responseJSON.setData(issue);

        return new ResponseEntity<ResponseJSON>(responseJSON, HttpStatus.OK);
    }

    @PutMapping(path = "/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseJSON> update(HttpServletRequest request,
                                         @RequestBody Issue issue,
                                         @PathVariable(name = "issueId") long issueId) throws IssueNotFoundException {
        log(request);
        issue = issueService.update(issueId, issue, request);

        ResponseJSON responseJSON = new ResponseJSON();
        responseJSON.setCode(ResponseConstant.OK);
        responseJSON.setData(issue);

        return new ResponseEntity<ResponseJSON>(responseJSON, HttpStatus.OK);
    }

    @DeleteMapping(path = "/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseJSON> delete(@PathVariable(name = "issueId") long issueId,
                                         HttpServletRequest request) throws IssueNotFoundException {
        log(request);
        issueService.delete(issueId, request);

        ResponseJSON responseJSON = new ResponseJSON();
        responseJSON.setCode(ResponseConstant.OK);

        return new ResponseEntity<ResponseJSON>(responseJSON, HttpStatus.OK);
    }

    @GetMapping(path = "/issues/{issueId}/approve", produces = {MediaType.APPLICATION_JSON_VALUE})
    public void approve(@RequestParam(name = "token") String token,
                             @PathVariable(name = "issueId") long issueId,
                             HttpServletRequest request) throws IssueNotFoundException {
        log(request);
        System.out.println(issueService.approve(issueId, token, request));
    }

    @GetMapping(path = "/issues/{issueId}/deny", produces = {MediaType.APPLICATION_JSON_VALUE})
    public void deny(@RequestParam(name = "token") String token,
                          @PathVariable(name = "issueId") long issueId,
                          HttpServletRequest request) throws IssueNotFoundException {
        log(request);
        System.out.println(issueService.deny(issueId, token, request));
    }
}

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
import vn.novahub.helpdesk.model.ResponseObject;
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


    @ExceptionHandler(IssueNotFoundException.class)
    public ResponseEntity<ResponseObject> handleIssueNotFoundException(HttpServletRequest request, Exception ex){
        ResponseObject responseObject = new ResponseObject();
        responseObject.setCode(ExceptionConstant.CODE_ISSUE_IS_NOT_EXIST);
        responseObject.setData(ExceptionConstant.MESSAGE_ISSUE_IS_NOT_EXIST);

        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
    }

    @GetMapping(path = "/issues", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseObject> getAllIssues(@RequestParam(name = "keyword", required = false) String keyword,
                                                       @RequestParam(name = "status", required = false) String status,
                                                       Pageable pageable,
                                                       HttpServletRequest request){
        logService.log(request, logger);
        Page<Issue> issuePage = issueService.getAllByKeyword(keyword, status, pageable, request);

        ResponseObject responseObject = new ResponseObject();
        responseObject.setCode(ResponseConstant.OK);
        responseObject.setData(issuePage);

        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
    }

    @GetMapping(path = "/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseObject> get(@PathVariable(name = "issueId") long issueId,
                                              HttpServletRequest request) throws IssueNotFoundException {
        logService.log(request, logger);
        Issue issue = issueService.getByIssueId(issueId, request);

        ResponseObject responseObject = new ResponseObject();
        responseObject.setCode(ResponseConstant.OK);
        responseObject.setData(issue);

        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
    }

    @GetMapping(path = "/accounts/issues", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseObject> getAllOfAnAccount(@RequestParam(name = "keyword", required = false) String keyword,
                                                            @RequestParam(name = "status", required = false) String status,
                                                            HttpServletRequest request,
                                                            Pageable pageable){
        logService.log(request, logger);
        Page<Issue> issuePage = issueService.getAllOfAccountByKeyword(keyword, status, pageable, request);

        ResponseObject responseObject = new ResponseObject();
        responseObject.setCode(ResponseConstant.OK);
        responseObject.setData(issuePage);

        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
    }

    @PostMapping(path = "/issues", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseObject> create(HttpServletRequest request,
                                                 @RequestBody Issue issue){
        logService.log(request, logger);
        issue = issueService.create(issue, request);

        ResponseObject responseObject = new ResponseObject();
        responseObject.setCode(ResponseConstant.OK);
        responseObject.setData(issue);

        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
    }

    @PutMapping(path = "/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseObject> update(HttpServletRequest request,
                                                 @RequestBody Issue issue,
                                                 @PathVariable(name = "issueId") long issueId) throws IssueNotFoundException {
        logService.log(request, logger);
        issue = issueService.update(issueId, issue, request);

        ResponseObject responseObject = new ResponseObject();
        responseObject.setCode(ResponseConstant.OK);
        responseObject.setData(issue);

        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
    }

    @DeleteMapping(path = "/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseObject> delete(@PathVariable(name = "issueId") long issueId,
                                                 HttpServletRequest request) throws IssueNotFoundException {
        logService.log(request, logger);
        issueService.delete(issueId, request);

        ResponseObject responseObject = new ResponseObject();
        responseObject.setCode(ResponseConstant.OK);

        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
    }

    @GetMapping(path = "/issues/{issueId}/approve", produces = {MediaType.APPLICATION_JSON_VALUE})
    public void approve(@RequestParam(name = "token") String token,
                        @PathVariable(name = "issueId") long issueId,
                        HttpServletRequest request) throws IssueNotFoundException {
        logService.log(request, logger);
        System.out.println(issueService.approve(issueId, token, request));
    }

    @GetMapping(path = "/issues/{issueId}/deny", produces = {MediaType.APPLICATION_JSON_VALUE})
    public void deny(@RequestParam(name = "token") String token,
                     @PathVariable(name = "issueId") long issueId,
                     HttpServletRequest request) throws IssueNotFoundException {
        logService.log(request, logger);
        System.out.println(issueService.deny(issueId, token, request));
    }
}

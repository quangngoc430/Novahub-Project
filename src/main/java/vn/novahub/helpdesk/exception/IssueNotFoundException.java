package vn.novahub.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Issue is not exist")
public class IssueNotFoundException extends Exception{

    public IssueNotFoundException(long issueId){
        super("IssueNotFoundException with id = " + issueId);
    }
}

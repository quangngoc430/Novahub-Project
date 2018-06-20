package vn.novahub.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class IssueNotFoundException extends Exception{

    public IssueNotFoundException(long issueId){
        super("IssueNotFoundException with id = " + issueId);
    }
}

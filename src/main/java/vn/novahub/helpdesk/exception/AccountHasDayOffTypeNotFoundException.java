package vn.novahub.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Day off type is not exist")
public class AccountHasDayOffTypeNotFoundException extends Exception{

    public AccountHasDayOffTypeNotFoundException(String type){
        super("AccountHasDayOffTypeNotFoundException with type = " + type);
    }

    public AccountHasDayOffTypeNotFoundException(long id) { super("AccountHasDayOffTypeNotFoundException with id = "+id); }
}

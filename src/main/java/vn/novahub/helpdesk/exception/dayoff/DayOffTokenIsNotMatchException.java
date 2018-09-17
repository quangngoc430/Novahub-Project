package vn.novahub.helpdesk.exception.dayoff;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Day off token is not match")
public class DayOffTokenIsNotMatchException extends Exception{

    public DayOffTokenIsNotMatchException(){
        super("Day off token is not match");
    }
}

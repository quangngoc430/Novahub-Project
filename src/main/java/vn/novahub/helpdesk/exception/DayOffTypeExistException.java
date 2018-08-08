package vn.novahub.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CREATED, reason = "Day off type was exist")
public class DayOffTypeExistException extends Exception{

    public DayOffTypeExistException(){
        super("Day off type was exist");
    }
}

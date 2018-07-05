package vn.novahub.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Day off type is not exist")
public class DayOffTypeNotFoundException extends Exception{

    public DayOffTypeNotFoundException(String type){
        super("DayOffTypeNotFoundException with type = " + type);
    }

    public DayOffTypeNotFoundException(long id) { super("DayOffTypeNotFoundException with id = "+id); }
}

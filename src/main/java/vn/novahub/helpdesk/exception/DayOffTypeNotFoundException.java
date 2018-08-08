package vn.novahub.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Common type is not exist")
public class DayOffTypeNotFoundException extends Exception{

    public DayOffTypeNotFoundException(){
        super("Common type is not exist");
    }
}

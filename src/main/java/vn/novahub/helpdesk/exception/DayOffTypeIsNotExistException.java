package vn.novahub.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Common type is not exist")
public class DayOffTypeIsNotExistException extends Exception{

    public DayOffTypeIsNotExistException(){
        super("Common type is not exist");
    }
}

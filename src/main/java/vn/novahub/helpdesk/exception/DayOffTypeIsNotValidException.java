package vn.novahub.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Day off type is not valid")
public class DayOffTypeIsNotValidException extends Exception{

    public DayOffTypeIsNotValidException(String type){
        super("Day off type is not valid with type = " + type);
    }
}

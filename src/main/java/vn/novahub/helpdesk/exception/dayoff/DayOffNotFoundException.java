package vn.novahub.helpdesk.exception.dayoff;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Day off is already answered")
public class DayOffNotFoundException extends Exception{

    public DayOffNotFoundException(long id){
        super("Day off with id = " + id + " is not exist");
    }
    public DayOffNotFoundException(String message){
        super(message);
    }

}

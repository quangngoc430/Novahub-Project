package vn.novahub.helpdesk.exception.dayoff;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Day off is overdue to delete")
public class DayOffOverdueException extends Exception{

    public DayOffOverdueException(long id){
        super("Day off with id = " + id + " is overdue to delete");
    }
}

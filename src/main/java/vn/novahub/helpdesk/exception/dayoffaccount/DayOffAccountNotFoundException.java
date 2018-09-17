package vn.novahub.helpdesk.exception.dayoffaccount;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "DayOffAccount is not exist")
public class DayOffAccountNotFoundException extends Exception{

    public DayOffAccountNotFoundException(String type){
        super("DayOffAccountNotFoundException with type = " + type);
    }

    public DayOffAccountNotFoundException(long id) { super("DayOffAccountNotFoundException with id = "+id); }
}

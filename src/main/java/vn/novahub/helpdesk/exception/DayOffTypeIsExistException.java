package vn.novahub.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Day off type is not exist")
public class DayOffTypeIsExistException extends Exception{

    public DayOffTypeIsExistException(int typeId){
        super("DayOffTypeIsExistException with type id = " + typeId);
    }
}

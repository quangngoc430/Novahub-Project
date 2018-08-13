package vn.novahub.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "DayOffAccount is exist")
public class DayOffAccountIsExistException extends Exception{

    public DayOffAccountIsExistException(int typeId){
        super("DayOffAccountIsExistException with type id = " + typeId);
    }
}

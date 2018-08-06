package vn.novahub.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "AccountHasDayOffType is exist")
public class AccountHasDayOffTypeIsExistException extends Exception{

    public AccountHasDayOffTypeIsExistException(int typeId){
        super("AccountHasDayOffTypeIsExistException with type id = " + typeId);
    }
}

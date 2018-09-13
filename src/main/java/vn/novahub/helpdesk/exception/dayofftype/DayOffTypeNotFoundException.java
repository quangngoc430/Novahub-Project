package vn.novahub.helpdesk.exception.dayofftype;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Day off type not found")
public class DayOffTypeNotFoundException extends Exception{

    public DayOffTypeNotFoundException(){
        super("Day off type not found");
    }
}

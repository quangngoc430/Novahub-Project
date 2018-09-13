package vn.novahub.helpdesk.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import vn.novahub.helpdesk.exception.dayoffaccount.DayOffAccountIsExistException;
import vn.novahub.helpdesk.exception.dayoffaccount.DayOffAccountNotFoundException;
import vn.novahub.helpdesk.exception.dayofftype.DayOffTypeExistException;
import vn.novahub.helpdesk.exception.dayofftype.DayOffTypeNotFoundException;
import vn.novahub.helpdesk.model.ApiError;
import vn.novahub.helpdesk.model.DayOffType;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@ControllerAdvice
public class DayOffTypeExceptionHandler {

    private ApiError apiError;

    @ExceptionHandler(value = DayOffTypeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleDayOffTypeNotFoundException(HttpServletRequest request, Exception ex){
        String message = "Day off type not found";
        HashMap<String, String> errors = new HashMap<>();
        errors.put("message", message);

        this.apiError = new ApiError(HttpStatus.NOT_FOUND.value(),
                                     errors,
                                     ex.getMessage(),
                                     request.getRequestURI());

        return new ResponseEntity<>(this.apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DayOffTypeExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiError> handleDayOffTypeIsExistException(HttpServletRequest request, Exception ex){
        String message = "Day off type is already exist";
        HashMap<String, String> errors = new HashMap<>();
        errors.put("message", message);

        this.apiError = new ApiError(HttpStatus.CONFLICT.value(),
                                     errors,
                                     ex.getMessage(),
                                     request.getRequestURI());

        return new ResponseEntity<>(this.apiError, HttpStatus.CONFLICT);
    }
}

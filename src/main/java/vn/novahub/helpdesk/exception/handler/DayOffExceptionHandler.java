package vn.novahub.helpdesk.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import vn.novahub.helpdesk.exception.dayoff.DayOffIsAnsweredException;
import vn.novahub.helpdesk.exception.dayoff.DayOffIsNotExistException;
import vn.novahub.helpdesk.exception.dayoff.DayOffOverdueException;
import vn.novahub.helpdesk.exception.dayoff.DayOffTokenIsNotMatchException;
import vn.novahub.helpdesk.model.ApiError;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@ControllerAdvice
public class DayOffExceptionHandler {

    private ApiError apiError;

    @ExceptionHandler(value = DayOffTokenIsNotMatchException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleDayOffTypeNotFoundException(HttpServletRequest request, Exception ex){
        String message = "Day off token is not match";
        HashMap<String, String> errors = new HashMap<>();
        errors.put("message", message);

        this.apiError = new ApiError(HttpStatus.NOT_FOUND.value(),
                                     errors,
                                     ex.getMessage(),
                                     request.getRequestURI());


        return new ResponseEntity<>(this.apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DayOffIsAnsweredException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleDayOffTypeIsExistException(HttpServletRequest request, Exception ex){
        String message = "Day off is answered exception";

        HashMap<String, String> errors = new HashMap<>();
        errors.put("message", message);

        this.apiError = new ApiError(HttpStatus.NOT_FOUND.value(),
                                     errors,
                                     ex.getMessage(),
                                     request.getRequestURI());

        return new ResponseEntity<>(this.apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DayOffOverdueException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleDayOffOverdueException(HttpServletRequest request, Exception ex){
        String message = "Day off is overdue exception";

        HashMap<String, String> errors = new HashMap<>();
        errors.put("message", message);

        this.apiError = new ApiError(HttpStatus.NOT_FOUND.value(),
                errors,
                ex.getMessage(),
                request.getRequestURI());

        return new ResponseEntity<>(this.apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DayOffIsNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleDayOffIsNotExistException(HttpServletRequest request, Exception ex){
        String message = "Day off is not exist exception";

        HashMap<String, String> errors = new HashMap<>();
        errors.put("message", message);

        this.apiError = new ApiError(HttpStatus.NOT_FOUND.value(),
                errors,
                ex.getMessage(),
                request.getRequestURI());

        return new ResponseEntity<>(this.apiError, HttpStatus.NOT_FOUND);
    }
}

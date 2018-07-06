package vn.novahub.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import vn.novahub.helpdesk.model.ApiError;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;

@ControllerAdvice
public class DayOffTypeExceptionHandler {

    private ApiError apiError;

    @ExceptionHandler(value = DayOffTypeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleDayOffTypeNotFoundException(HttpServletRequest request, Exception ex){
        String message = "Day off type not found";

        setValueForApiError(message, request, ex);
        this.apiError.setStatus(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(this.apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DayOffTypeIsExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiError> handleDayOffTypeIsExistException(HttpServletRequest request, Exception ex){
        String message = "Day off type is already exist";

        setValueForApiError(message, request, ex);
        this.apiError.setStatus(HttpStatus.CONFLICT.value());

        return new ResponseEntity<>(this.apiError, HttpStatus.CONFLICT);
    }

    private void setValueForApiError(String message, HttpServletRequest request, Exception ex) {
        this.apiError = new ApiError();
        apiError.setTimestamp(Instant.now());
        HashMap<String, String> errors = new HashMap<>();
        errors.put("message", message);
        apiError.setErrors(errors);
        apiError.setPath(request.getRequestURI());
        apiError.setMessage(ex.getMessage());
    }

}

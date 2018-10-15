package vn.novahub.helpdesk.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import vn.novahub.helpdesk.model.ApiError;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;

@ControllerAdvice
public class ValidationExceptionHandler {

    private ApiError apiError;

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleDayOffTypeNotFoundException(HttpServletRequest request, Exception ex){
        String message = "Constraint violation exception";
        HashMap<String, String> errors = new HashMap<>();
        errors.put("message", message);

        this.apiError = new ApiError(HttpStatus.BAD_REQUEST.value(),
                                     errors,
                                     ex.getMessage(),
                                     request.getRequestURI());


        return new ResponseEntity<>(this.apiError, HttpStatus.BAD_REQUEST);
    }
}

package vn.novahub.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.novahub.helpdesk.model.ApiError;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class IssueExceptionHandler {

    @ExceptionHandler(value = IssueNotFoundException.class)
    public ResponseEntity<ApiError> handleIssueNotFoundException(HttpServletRequest request, Exception ex){
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(),
                                        HttpStatus.NOT_FOUND.name(),
                                        request.getRequestURI(),
                                        ex.getMessage());
        return new ResponseEntity<ApiError>(apiError, HttpStatus.NOT_FOUND);
    }

}

package vn.novahub.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.novahub.helpdesk.model.ApiError;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;

@ControllerAdvice
public class IssueExceptionHandler {

    @ExceptionHandler(value = IssueNotFoundException.class)
    public ResponseEntity<ApiError> handleIssueNotFoundException(HttpServletRequest request, Exception ex) {
        ApiError apiError = new ApiError();

        apiError.setTimestamp(Instant.now());
        apiError.setStatus(HttpStatus.NOT_FOUND.value());
        HashMap<String, String> errors = new HashMap<>();
        errors.put("message", "Issue not found");
        apiError.setError(errors);
        apiError.setPath(request.getRequestURI());
        apiError.setMessage(ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IssueValidationException.class)
    public ResponseEntity<ApiError> handleIssueValidationException(HttpServletRequest request, Exception ex) {
        ApiError apiError = new ApiError();

        apiError.setTimestamp(Instant.now());
        apiError.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        apiError.setError(((IssueValidationException) ex).getErrors());
        apiError.setPath(request.getRequestURI());
        apiError.setMessage(ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = IssueIsClosedException.class)
    public ResponseEntity<ApiError> handleIssueIsCloseException(HttpServletRequest request, Exception ex) {
        ApiError apiError = new ApiError();

        apiError.setTimestamp(Instant.now());
        apiError.setStatus(HttpStatus.LOCKED.value());
        HashMap<String, String> errors = new HashMap<>();
        errors.put("message", "Issue is closed");
        apiError.setError(errors);
        apiError.setPath(request.getRequestURI());
        apiError.setMessage(ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.LOCKED);
    }
}

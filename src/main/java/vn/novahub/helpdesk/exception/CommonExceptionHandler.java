package vn.novahub.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.novahub.helpdesk.model.ApiError;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;

@ControllerAdvice
public class CommonExceptionHandler {

    private static String MESSAGE = "message";

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorizedException(HttpServletRequest request, Exception exception) {
        ApiError apiError = new ApiError();

        apiError.setTimestamp(Instant.now());
        apiError.setStatus(HttpStatus.UNAUTHORIZED.value());
        HashMap<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, "Unauthorized");
        apiError.setError(errors);
        apiError.setMessage(exception.getMessage());
        apiError.setPath(request.getRequestURI());

        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(HttpServletRequest request, Exception exception) {
        ApiError apiError = new ApiError();

        apiError.setTimestamp(Instant.now());
        apiError.setStatus(HttpStatus.FORBIDDEN.value());
        HashMap<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, "Access is denied");
        apiError.setError(errors);
        apiError.setMessage(exception.getMessage());
        apiError.setPath(request.getRequestURI());

        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

}

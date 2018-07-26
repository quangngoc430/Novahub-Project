package vn.novahub.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.novahub.helpdesk.model.ApiError;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class LevelExceptionHandler {

    @ExceptionHandler(value = LevelValidationException.class)
    public ResponseEntity<ApiError> handleLevelValidationException(HttpServletRequest request, Exception exception) {
        ApiError apiError = new ApiError();

        apiError.setTimestamp(Instant.now());
        apiError.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        apiError.setError(((LevelValidationException) exception).getErrors());
        apiError.setMessage(exception.getMessage());
        apiError.setPath(request.getRequestURI());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_ACCEPTABLE);
    }
}

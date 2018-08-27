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

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorizedException(HttpServletRequest request, Exception exception) {

        String requestURI = ((UnauthorizedException) exception).getUrl();
        requestURI = (requestURI == null) ? request.getRequestURI() : requestURI;
        return new ResponseEntity<>(createApiError(HttpStatus.UNAUTHORIZED, "Unauthorized", requestURI, exception ), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(HttpServletRequest request, Exception exception) {
        return new ResponseEntity<>(createApiError(HttpStatus.FORBIDDEN, "Access is denied", request.getRequestURI(), exception),
                                    HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(HttpServletRequest request, Exception exception) {
        return new ResponseEntity<>(createApiError(HttpStatus.BAD_REQUEST, "Bad request", request.getRequestURI(), exception),
                                    HttpStatus.BAD_REQUEST);
    }

    private ApiError createApiError(HttpStatus httpStatus, String message, String requestURI, Exception exception) {
        ApiError apiError = new ApiError();

        apiError.setTimestamp(Instant.now());
        apiError.setStatus(httpStatus.value());
        HashMap<String, String> errors = new HashMap<>();
        errors.put("message", message);
        apiError.setError(errors);
        apiError.setMessage(exception.getMessage());
        apiError.setPath(requestURI);

        return apiError;
    }

}

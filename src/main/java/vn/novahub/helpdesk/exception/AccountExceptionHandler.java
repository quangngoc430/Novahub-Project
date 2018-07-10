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
public class AccountExceptionHandler {

    private static final String MESSAGE = "message";

    @ExceptionHandler(value = AccountNotFoundException.class)
    public ResponseEntity<ApiError> handleAccountNotFoundException(HttpServletRequest request, Exception ex){

        ApiError apiError = new ApiError();

        apiError.setTimestamp(Instant.now());
        apiError.setStatus(HttpStatus.NOT_FOUND.value());
        HashMap<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, "Account not found");
        apiError.setErrors(errors);
        apiError.setPath(request.getRequestURI());
        apiError.setMessage(ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = AccountIsExistException.class)
    public ResponseEntity<ApiError> handleAccountIsExistException(HttpServletRequest request, Exception ex){
        ApiError apiError = new ApiError();

        apiError.setTimestamp(Instant.now());
        apiError.setStatus(HttpStatus.CONFLICT.value());
        HashMap<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, "Account is exist");
        apiError.setErrors(errors);
        apiError.setPath(request.getRequestURI());
        apiError.setMessage(ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = AccountValidationException.class)
    public ResponseEntity<ApiError> handleAccountValidationException(HttpServletRequest request, Exception ex){
        ApiError apiError = new ApiError();

        apiError.setTimestamp(Instant.now());
        apiError.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        apiError.setErrors(((AccountValidationException) ex).getErrors());
        apiError.setPath(request.getRequestURI());
        apiError.setMessage(ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = AccountPasswordNotEqualException.class)
    public ResponseEntity<ApiError> handleAccountPasswordNotEqualException(HttpServletRequest request, Exception ex){
        ApiError apiError = new ApiError();

        apiError.setTimestamp(Instant.now());
        apiError.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        HashMap<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, "Two passwords do not match");
        apiError.setErrors(errors);
        apiError.setPath(request.getRequestURI());
        apiError.setMessage(ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = AccountInvalidException.class)
    public ResponseEntity<ApiError> handleAccountInvalidException(HttpServletRequest request, Exception ex){
        ApiError apiError = new ApiError();

        apiError.setTimestamp(Instant.now());
        apiError.setStatus(HttpStatus.NOT_FOUND.value());
        HashMap<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, "Invalid email or password");
        apiError.setErrors(errors);
        apiError.setPath(request.getRequestURI());
        apiError.setMessage(ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = AccountInactiveException.class)
    public ResponseEntity<ApiError> handleAccountInactiveException(HttpServletRequest request, Exception ex) {
        ApiError apiError = new ApiError();

        apiError.setTimestamp(Instant.now());
        apiError.setStatus(HttpStatus.FORBIDDEN.value());
        HashMap<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, "Inactive email");
        apiError.setErrors(errors);
        apiError.setPath(request.getRequestURI());
        apiError.setMessage(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = AccountLockedException.class)
    public ResponseEntity<ApiError> handleAccountLockedException(HttpServletRequest request, Exception ex){
        ApiError apiError = new ApiError();

        apiError.setTimestamp(Instant.now());
        apiError.setStatus(HttpStatus.LOCKED.value());
        HashMap<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, "User is locked");
        apiError.setErrors(errors);
        apiError.setPath(request.getRequestURI());
        apiError.setMessage(ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.LOCKED);
    }

}

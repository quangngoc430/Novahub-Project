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
public class SkillExceptionHandler {

    @ExceptionHandler(value = SkillNotFoundException.class)
    public ResponseEntity<ApiError> handleSkillNotFoundException(HttpServletRequest request, Exception ex){
        ApiError apiError = new ApiError();

        apiError.setTimestamp(Instant.now());
        apiError.setStatus(HttpStatus.NOT_FOUND.value());
        HashMap<String, String> errors = new HashMap<>();
        errors.put("message", "Skill not found");
        apiError.setErrors(errors);
        apiError.setPath(request.getRequestURI());
        apiError.setMessage(ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(value = SkillIsExistException.class)
    public ResponseEntity<ApiError> handleSkillIsExistException(HttpServletRequest request, Exception ex){
        ApiError apiError = new ApiError();

        apiError.setTimestamp(Instant.now());
        apiError.setStatus(HttpStatus.CONFLICT.value());
        HashMap<String, String> errors = new HashMap<>();
        errors.put("message", "Skill is exist");
        apiError.setErrors(errors);
        apiError.setPath(request.getRequestURI());
        apiError.setMessage(ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = SkillValidationException.class)
    public ResponseEntity<ApiError> handleSkillValidationException(HttpServletRequest request, Exception ex){
        ApiError apiError = new ApiError();

        apiError.setTimestamp(Instant.now());
        apiError.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        apiError.setErrors(((SkillValidationException) ex).getErrors());
        apiError.setPath(request.getRequestURI());
        apiError.setMessage(ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_ACCEPTABLE);
    }

}

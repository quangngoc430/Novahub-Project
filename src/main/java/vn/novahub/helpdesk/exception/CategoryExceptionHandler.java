package vn.novahub.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.novahub.helpdesk.model.ApiError;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class CategoryExceptionHandler {

    @ExceptionHandler(value = CategoryNotFoundException.class)
    public ResponseEntity<ApiError> handleCategoryNotFoundException(HttpServletRequest request, Exception ex){
        ApiError apiError = new ApiError();
        apiError.setTimestamp(Instant.now());
        apiError.setStatus(HttpStatus.NOT_FOUND.value());
        apiError.setError(HttpStatus.NOT_FOUND.name());
        apiError.setMessage(ex.getMessage());
        apiError.setPath(request.getRequestURI());

        return new ResponseEntity<ApiError>(apiError, HttpStatus.NOT_FOUND);
    }

}

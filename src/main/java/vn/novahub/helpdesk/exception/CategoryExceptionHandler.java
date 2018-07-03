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
public class CategoryExceptionHandler {

    @ExceptionHandler(value = CategoryNotFoundException.class)
    public ResponseEntity<ApiError> handleCategoryNotFoundException(HttpServletRequest request, Exception ex){
        ApiError apiError = new ApiError();

        apiError.setTimestamp(Instant.now());
        apiError.setStatus(HttpStatus.NOT_FOUND.value());
        HashMap<String, String> errors = new HashMap<>();
        errors.put("message", "Category not found");
        apiError.setErrors(errors);
        apiError.setPath(request.getRequestURI());
        apiError.setMessage(ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

}

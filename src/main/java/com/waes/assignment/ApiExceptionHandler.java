package com.waes.assignment;

import com.waes.assignment.model.exceptions.ApiError;
import com.waes.assignment.model.exceptions.ResourceNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException resourceNotFound) {

        ApiError apiError = new ApiError();
        apiError.setDetail(resourceNotFound.getMessage());
        return new ResponseEntity<ApiError>(apiError, HttpStatus.NOT_FOUND);
    }
}

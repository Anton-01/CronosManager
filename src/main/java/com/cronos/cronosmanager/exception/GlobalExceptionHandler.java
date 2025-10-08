package com.cronos.cronosmanager.exception;

import com.cronos.cronosmanager.exception.common.ResourceNotFoundException;
import com.cronos.cronosmanager.model.response.ResponseApi;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseApi<Void>> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        logger.error("Data Integrity error. Caused by : {}", ex.getMostSpecificCause().getMessage());
        String message = "A data conflict occurred. A provided value may already exist or violates a required constraint.";
        ResponseApi<Void> response = new ResponseApi<>(HttpStatus.CONFLICT, message);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseApi<Void>> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        ResponseApi<Void> response = new ResponseApi<>(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ResponseApi<Void>> handleEntityExist(EntityExistsException ex, WebRequest request) {
        ResponseApi<Void> response = new ResponseApi<>(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseApi<Object>> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        String errors = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining(", "));

        String message = "Error of validation: " + errors;
        ResponseApi<Object> response = new ResponseApi<>(HttpStatus.BAD_REQUEST, message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseApi<Void>> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        ResponseApi<Void> response = new ResponseApi<>(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseApi<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String nameField = ((FieldError) error).getField();
            String messageError = error.getDefaultMessage();
            errors.put(nameField, messageError);
        });

        ResponseApi<Object> response = new ResponseApi<>(HttpStatus.BAD_REQUEST, "Validation error in the Request.");
        response.setData(errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseApi<Void>> handleGlobalException(Exception ex, WebRequest request) {
        String message = "An unexpected error occurred on the server. Please try again later.";
        ResponseApi<Void> response = new ResponseApi<>(HttpStatus.INTERNAL_SERVER_ERROR, message);
        logger.error("Uncontrolled error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

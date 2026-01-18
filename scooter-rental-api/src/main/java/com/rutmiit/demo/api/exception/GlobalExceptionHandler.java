package com.rutmiit.demo.api.exception;

import com.rutmiit.demo.api.dto.StatusResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StatusResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new StatusResponse("error", ex.getMessage()));
    }

    @ExceptionHandler(ScooterAlreadyExistsException.class)
    public ResponseEntity<StatusResponse> handleScooterAlreadyExists(ScooterAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new StatusResponse("error", ex.getMessage()));
    }

    @ExceptionHandler(ScooterUnavailableException.class)
    public ResponseEntity<StatusResponse> handleScooterUnavailable(ScooterUnavailableException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new StatusResponse("error", ex.getMessage()));
    }

    @ExceptionHandler(RentalAlreadyFinishedException.class)
    public ResponseEntity<StatusResponse> handleRentalAlreadyFinished(RentalAlreadyFinishedException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new StatusResponse("error", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StatusResponse> handleValidation(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new StatusResponse("error", errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StatusResponse> handleGeneralError(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new StatusResponse("error", "Internal server error: " + ex.getMessage()));
    }
}


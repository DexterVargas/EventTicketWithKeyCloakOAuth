package com.dexterv.eventticket.exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value={Exception.class})
    public ResponseEntity<ErrorDto> handleException(Exception ex){
        log.error(ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("An unknown error occurred");
        return  new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException ex){
        log.error(ex.getMessage(), ex);

        String errorMessage = ex.getConstraintViolations()
                .stream()
                .findFirst()
                .map(violation ->
                        violation.getPropertyPath() + ": "+ violation.getMessage())
                .orElse("A constraint violation occurred");

        ErrorDto errorDto = new ErrorDto();
        errorDto.setError(errorMessage);
        return  new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException ex){
        log.error(ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("User not found");
        return  new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

}

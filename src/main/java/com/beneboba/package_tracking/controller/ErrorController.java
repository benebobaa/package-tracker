package com.beneboba.package_tracking.controller;


import com.beneboba.package_tracking.model.BaseResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@Slf4j
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse<String>> constraintViolationException(ConstraintViolationException exception){

        log.error("constraintViolationException -> " + exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.<String>builder()
                        .errors(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<BaseResponse<String>> responseStatusException(ResponseStatusException exception){

        log.error("responseStatusException -> " + exception.getReason());

        return ResponseEntity
                .status(exception.getStatusCode())
                .body(BaseResponse.<String>builder()
                        .errors(exception.getReason())
                        .build());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<BaseResponse<String>> authenticationException(AuthenticationException exception) {

        log.error("authenticationException -> " + exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse.<String>builder()
                        .errors(exception.getMessage())
                        .build());
    }
}

package com.education.microservices.broker.api.controller;

import com.education.microservices.broker.api.exception.NotFoundFigiException;
import com.education.microservices.broker.api.exception.response.ExResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundFigiException.class)
    public ResponseEntity<ExResponse> handleNotFoundFigiException(NotFoundFigiException e) {
        return new ResponseEntity<>(new ExResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExResponse> handleException(Exception e) {
        return new ResponseEntity<>(new ExResponse(e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
    }
}

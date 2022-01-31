package com.education.microservices.currencyexchangeservice.exception;

import com.education.microservices.currencyexchangeservice.exception.response.ExResponse;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExeptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ExResponse> feignException(Exception e) {
        return new ResponseEntity<>(new ExResponse(e.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);
    }
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExResponse> customException(Exception e) {
        return new ResponseEntity<>(new ExResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}

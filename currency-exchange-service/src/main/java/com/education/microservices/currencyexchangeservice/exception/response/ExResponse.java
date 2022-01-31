package com.education.microservices.currencyexchangeservice.exception.response;

public class ExResponse {
    private String message;

    public ExResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

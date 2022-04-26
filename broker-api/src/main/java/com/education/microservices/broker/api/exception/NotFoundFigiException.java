package com.education.microservices.broker.api.exception;

import java.util.Map;

public class NotFoundFigiException extends RuntimeException {

    private String message;

    private Map<String, Object> params;

    public NotFoundFigiException(String message, Map<String, Object> params) {
        this.message = message;
        this.params = params;
    }

    public static NotFoundFigiExceptionBuilder builder() {
        return new NotFoundFigiExceptionBuilder();
    }
}

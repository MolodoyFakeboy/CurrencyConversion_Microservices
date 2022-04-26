package com.education.microservices.broker.api.exception;

import java.util.HashMap;
import java.util.Map;

public class NotFoundFigiExceptionBuilder {

    private Map<String, Object> params;
    private String message;

    public NotFoundFigiExceptionBuilder message(String message) {
        this.message = message;
        return this;
    }

    public NotFoundFigiExceptionBuilder addParam(String name, Object param) {
        if (this.params == null) {
            this.params = new HashMap<>();
        }

        this.params.put(name, param);

        return this;
    }

    public NotFoundFigiException build() {
        return new NotFoundFigiException(message, params);
    }

}

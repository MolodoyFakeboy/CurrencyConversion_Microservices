package com.education.microservices.currencyexchangeservice.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Response implements Serializable {

    private double rubRate;

    private double rateFromRequest;

}

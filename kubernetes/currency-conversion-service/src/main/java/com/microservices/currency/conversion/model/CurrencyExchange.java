package com.microservices.currency.conversion.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CurrencyExchange implements Serializable {

    @JsonProperty("rubRate")
    private double rubRate;

    @JsonProperty("rateFromRequest")
    private double rateFromRequest;

}

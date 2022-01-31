package com.education.microservices.currencyexchangeservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeRate {

    @JsonProperty(value = "timestamp")
    private long timestamp;
    @JsonProperty(value = "base")
    private String base;
    @JsonProperty(value = "rates")
    private Map<String, Double> rates;

}

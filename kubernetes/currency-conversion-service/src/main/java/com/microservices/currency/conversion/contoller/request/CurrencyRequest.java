package com.microservices.currency.conversion.contoller.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CurrencyRequest implements Serializable {

    private String baseCurrency;

    private int amount;
}

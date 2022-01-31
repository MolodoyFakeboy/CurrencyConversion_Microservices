package com.microservices.currency.conversion.client;

import com.microservices.currency.conversion.model.CurrencyExchange;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "currency-exchange")
public interface CurrencyExchangeClient {

    @GetMapping("/currency/exchange/{currency}")
    CurrencyExchange getCurrencyExchange(@PathVariable (value ="currency") String currency);

}

package com.education.microservices.currencyexchangeservice.client;

import com.education.microservices.currencyexchangeservice.model.ExchangeRate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "currency", url = "${currency.url}")
public interface CurrencyApi  {

    @GetMapping("latest.json?app_id=${currency.api_key}")
    ExchangeRate getTodayExchangeRate();
}

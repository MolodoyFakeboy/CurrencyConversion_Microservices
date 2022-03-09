package com.education.microservices.currencyexchangeservice.controller;

import com.education.microservices.currencyexchangeservice.model.Response;
import com.education.microservices.currencyexchangeservice.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("currency/exchange")
public class CurrencyExchangeController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping(value = "/{currency}")
    public ResponseEntity<Response> getExchageRates(@PathVariable String currency) {
        return ResponseEntity.ok(currencyService.getTodayExchangeRate(currency));
    }

}

package com.education.microservices.currencyexchangeservice.service;

import com.education.microservices.currencyexchangeservice.client.CurrencyApi;
import com.education.microservices.currencyexchangeservice.exception.CustomException;
import com.education.microservices.currencyexchangeservice.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CurrencyService {

    private final CurrencyApi currencyApi;

    @Autowired
    public CurrencyService(CurrencyApi currencyApi) {
        this.currencyApi = currencyApi;
    }

    public Response getTodayExchangeRate(String rate) {
        var exchangeRates = currencyApi.getTodayExchangeRate().getRates();
        log.info("Получаем курс валют на сегодня");
        if (exchangeRates.get(rate.toUpperCase()) != null) {
            return Response.builder()
                    .rubRate(exchangeRates.get("RUB"))
                    .rateFromRequest(exchangeRates.get(rate.toUpperCase()))
                    .build();
        } else {
            throw new CustomException("Not exist");
        }
    }

}

package com.microservices.currency.conversion.service;

import com.microservices.currency.conversion.client.CurrencyExchangeClient;
import com.microservices.currency.conversion.contoller.request.CurrencyRequest;
import com.microservices.currency.conversion.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Slf4j
public class CurrencyConversionService {

    @Autowired
    private CurrencyExchangeClient client;

    public Response calculateCurrency(CurrencyRequest currencyRequest) {
        var currencyExchange = client.getCurrencyExchange(currencyRequest.getBaseCurrency());

        double rubToBaseCurrency = currencyExchange.getRateFromRequest() / currencyExchange.getRubRate();

        var sum = BigDecimal.valueOf(rubToBaseCurrency * currencyRequest.getAmount());

        log.info("Расчет прошел успешно");

        return Response.builder()
                .date(LocalDate.now())
                .rubRate(rubToBaseCurrency)
                .sum(sum)
                .build();
    }

}

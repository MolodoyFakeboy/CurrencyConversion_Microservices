package com.microservices.currency.conversion.contoller;

import com.microservices.currency.conversion.contoller.request.CurrencyRequest;
import com.microservices.currency.conversion.service.CurrencyConversionService;
import com.microservices.currency.conversion.model.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("currency/conversion")
@RequiredArgsConstructor
public class CurrencyConversionController {

    private final CurrencyConversionService conversionService;

    @GetMapping
    public ResponseEntity<Response> getCurrencyConversionResult(
            @RequestParam(required = false, defaultValue = "100") int amount,
            @RequestParam(required = false, defaultValue = "USD") String baseCurrency) {

        var request = CurrencyRequest.builder()
                .amount(amount)
                .baseCurrency(baseCurrency)
                .build();

        return ResponseEntity.ok(conversionService.calculateCurrency(request));
    }

}

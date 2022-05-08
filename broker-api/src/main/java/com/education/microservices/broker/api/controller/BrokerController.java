package com.education.microservices.broker.api.controller;

import com.education.microservices.broker.api.dto.ShareDto;
import com.education.microservices.broker.api.dto.TopShareDto;
import com.education.microservices.broker.api.model.ShareWithReference;
import com.education.microservices.broker.api.service.BrokerService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/broker/api/shares")
public class BrokerController {

    private final BrokerService brokerService;

    @PostMapping("/{ticker}")
    public ResponseEntity<ShareDto> addNewShare(@PathVariable String ticker) {
        return ResponseEntity.ok(brokerService.findNeedFigiAndAddToMongo(ticker));
    }

    @GetMapping("/name/{ticker}")
    public ResponseEntity<List<ShareDto>> findByTicker(@PathVariable String ticker) {
        return ResponseEntity.ok(brokerService.findShareByName(ticker.toUpperCase(Locale.ROOT)));
    }

    @GetMapping("/time/{date}")
    public ResponseEntity<List<ShareDto>> findByTime(@PathVariable String date) {
        return ResponseEntity.ok(brokerService.findShareByTime(date));
    }

    @GetMapping("")
    public ResponseEntity<List<ShareDto>> findByTimeAndTicker(@RequestParam String time,
                                                              @RequestParam String ticker) {
        return ResponseEntity.ok(brokerService.findShareByNameAndDateQuery(
                ticker.toUpperCase(Locale.ROOT),time));
    }

    @GetMapping("/risk")
    public ResponseEntity<List<ShareDto>> findNeedShareForUser() {
        return ResponseEntity.ok(brokerService.findNeedFigiForUser());
    }

    @GetMapping("/name/reference/{ticker}")
    public ResponseEntity<ShareWithReference> findReferenceByTicker(@PathVariable String ticker) {
        return ResponseEntity.ok(brokerService.findShareWithReferenceByName(ticker.toUpperCase(Locale.ROOT)));
    }

    @GetMapping("/day/top")
    public ResponseEntity<List<TopShareDto>>findTopOfTheDay(@RequestParam @DateTimeFormat(iso = DATE) LocalDate date) {
        return ResponseEntity.ok(brokerService.findTopOfTheDay(date));
    }


}

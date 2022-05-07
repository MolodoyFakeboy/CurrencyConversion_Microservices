package com.education.microservices.broker.api.service;

import com.education.microservices.broker.api.exception.NotFoundFigiException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.tinkoff.piapi.core.InvestApi;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class TinkoffApiService {

    private final InvestApi investApi;

    @SneakyThrows
    public Share findNeedRuFigiStockByTicker(String ticker) {
        return investApi.getInstrumentsService().getAllShares().get()
                .stream()
                .filter(share -> share.getTicker().equals(ticker.toUpperCase(Locale.ROOT)))
                .filter(share -> share.getCurrency().equals("rub"))
                .findFirst()
                .orElseThrow(() -> NotFoundFigiException.builder()
                        .addParam("Ticker", ticker)
                        .message("Акция не найдена по названию").build());
    }

    public HistoricCandle getDayCandleForFigi(String stockFigi) {
        return investApi.getMarketDataService()
                .getCandlesSync(stockFigi, Instant.now().minus(3, ChronoUnit.DAYS), Instant.now(), CandleInterval.CANDLE_INTERVAL_DAY)
                .stream().findFirst()
                .orElse(null);
    }

}

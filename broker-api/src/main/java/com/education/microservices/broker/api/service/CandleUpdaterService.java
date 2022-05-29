package com.education.microservices.broker.api.service;

import com.education.microservices.broker.api.model.Candle;
import com.education.microservices.broker.api.repository.CandleRepository;
import com.education.microservices.broker.api.repository.ShareWithReferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;

import java.util.List;

import static ru.tinkoff.piapi.core.utils.DateUtils.timestampToString;
import static ru.tinkoff.piapi.core.utils.MapperUtils.quotationToBigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class CandleUpdaterService {

    private final ShareWithReferenceRepository shareWithReferenceRepository;
    private final CandleRepository candleRepository;
    private final TinkoffApiService tinkoffApiService;

    @Scheduled(cron = "${crone.expression.value}")
    public void candleTaskExecutor(){
        var shares = shareWithReferenceRepository.findAll();

        log.info("Начинаем обновление котировок");

        shares.forEach(share -> {
            var stockFigi = share.getFigi();
            var candleDay = tinkoffApiService.getDayCandleForFigi(stockFigi);

            if (candleDay != null) {
                var candle = candleRepository.save(buildCandle(candleDay));

                if (share.getCandles() != null) {
                    share.getCandles().add(candle);
                } else {
                    share.setCandles(List.of(candle));
                }
            }

            shareWithReferenceRepository.save(share);
        });
        log.info("Закончил обновление");
    }

    private Candle buildCandle(HistoricCandle historicCandle) {
        return Candle.builder()
                .openPrice(quotationToBigDecimal(historicCandle.getOpen()))
                .closePrice(quotationToBigDecimal(historicCandle.getClose()))
                .highDailyPrice(quotationToBigDecimal(historicCandle.getHigh()))
                .lowestDailyPrice(quotationToBigDecimal(historicCandle.getLow()))
                .time(timestampToString(historicCandle.getTime()))
                .build();
    }

}

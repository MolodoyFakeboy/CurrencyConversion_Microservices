package com.education.microservices.broker.api.service;

import com.education.microservices.broker.api.dto.ShareDto;
import com.education.microservices.broker.api.exception.NotFoundFigiException;
import com.education.microservices.broker.api.mapper.ShareMapper;
import com.education.microservices.broker.api.repository.ShareRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.tinkoff.piapi.core.InvestApi;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static ru.tinkoff.piapi.core.utils.DateUtils.timestampToString;
import static ru.tinkoff.piapi.core.utils.MapperUtils.quotationToBigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrokerService {

    private final InvestApi investApi;

    private final ShareRepository shareRepository;

    private final ShareMapper shareMapper;

    private final RabbitMQService rabbitMQService;

    public ShareDto findNeedFigiAndAddToMongo(String ticker) {
        var share = findNeedRuFigiStock(ticker);
        var stockFigi = share.getFigi();
        var candleDay = getDayCandleForFigi(stockFigi);
        var shareDto = buildShareFormat(share,candleDay,stockFigi);
        saveToMongo(shareDto);
        return shareDto;
    }

    @SneakyThrows
    private Share findNeedRuFigiStock(String ticker) {
        return investApi.getInstrumentsService().getAllShares().get()
                .stream()
                .filter(share -> share.getTicker().equals(ticker.toUpperCase(Locale.ROOT)))
                .filter(share -> share.getCurrency().equals("rub"))
                .findFirst()
                .orElseThrow(() -> NotFoundFigiException.builder()
                        .addParam("Ticker", ticker)
                        .message("Акция не найдена по названию").build());
    }

    private HistoricCandle getDayCandleForFigi(String stockFigi){
        return investApi.getMarketDataService()
                .getCandlesSync(stockFigi, Instant.now().minus(3, ChronoUnit.DAYS), Instant.now(), CandleInterval.CANDLE_INTERVAL_DAY)
                .stream().findFirst()
                .orElseThrow();
    }

    private ShareDto buildShareFormat(Share share, HistoricCandle historicCandle,String figi) {
        return ShareDto.builder()
                .figi(figi)
                .shortName(share.getTicker())
                .fullName(share.getName())
                .country(share.getCountryOfRisk())
                .openPrice(quotationToBigDecimal(historicCandle.getOpen()))
                .closePrice(quotationToBigDecimal(historicCandle.getClose()))
                .highDailyPrice(quotationToBigDecimal(historicCandle.getHigh()))
                .lowestDailyPrice(quotationToBigDecimal(historicCandle.getLow()))
                .time(timestampToString(historicCandle.getTime()))
                .build();
    }

    private void saveToMongo(ShareDto shareDto){
        shareRepository.save(shareMapper.toEntity(shareDto));
    }

    public List<ShareDto> findShareByName(String name){
        return shareRepository.findByShortName(name)
                .stream()
                .map(shareMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ShareDto> findShareByTime(String time){
        return shareRepository.findByTimeRegex(time)
                .stream()
                .map(shareMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ShareDto> findShareByNameAndDateQuery(String name,String time){
        return shareMapper.toListDto
                (shareRepository.findByNameAndTime(name, time));
    }

    public List<ShareDto> findNeedFigiForUser() {
        return rabbitMQService.sendNeedShare();
    }

    public void findOneFigiForUser() {
        rabbitMQService.sendOneShare();
    }

}

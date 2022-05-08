package com.education.microservices.broker.api.service;

import com.education.microservices.broker.api.dto.ShareDto;
import com.education.microservices.broker.api.dto.TopShareDto;
import com.education.microservices.broker.api.exception.NotFoundFigiException;
import com.education.microservices.broker.api.mapper.ShareMapper;
import com.education.microservices.broker.api.model.Candle;
import com.education.microservices.broker.api.model.ShareWithReference;
import com.education.microservices.broker.api.repository.CandleRepository;
import com.education.microservices.broker.api.repository.ShareRepository;
import com.education.microservices.broker.api.repository.ShareWithReferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.contract.v1.Share;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.tinkoff.piapi.core.utils.DateUtils.timestampToString;
import static ru.tinkoff.piapi.core.utils.MapperUtils.quotationToBigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrokerService {

    private final ShareRepository shareRepository;
    private final ShareMapper shareMapper;
    private final RabbitMQService rabbitMQService;
    private final TinkoffApiService tinkoffApiService;
    private final ShareWithReferenceRepository shareWithReferenceRepository;
    private final MongoTemplate mongoTemplate;

    private final CandleRepository candleRepository;

    private final CandleUpdaterService candleUpdaterService;

    public ShareDto findNeedFigiAndAddToMongo(String ticker) {
        var share = tinkoffApiService.findNeedRuFigiStockByTicker(ticker);
        var stockFigi = share.getFigi();
        var candleDay = tinkoffApiService.getDayCandleForFigi(stockFigi);
        var shareDto = buildShareFormat(share,candleDay,stockFigi);
        saveToMongo(shareDto);
        return shareDto;
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

    public ShareWithReference findShareWithReferenceByName(String name){
        return shareWithReferenceRepository.getByShortName(name);
    }

    public List<TopShareDto> findTopOfTheDay(LocalDate date) {
        Query query = new Query();
        query.addCriteria(Criteria.where("time").regex(date.toString()));
        var candles = mongoTemplate.find(query, Candle.class);

        List<TopShareDto> topShares = new ArrayList<>();
        candles.forEach(candle -> {
            var openPrice = candle.getOpenPrice();

            var percentOfGrowth = (candle.getClosePrice().subtract(openPrice))
                    .divide(openPrice,3,RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

            var shareName = shareWithReferenceRepository.getByCandleId(candle.getId())
                    .stream().findFirst().orElseThrow(
                            () -> NotFoundFigiException.builder()
                            .message("Can not find Share by candle")
                            .addParam("ID", candle.getId())
                            .build()).getShortName();

            var topShare = TopShareDto.builder()
                    .percentOfGrowth(percentOfGrowth)
                    .shortName(shareName)
                    .build();

            topShares.add(topShare);
        });

        return topShares.stream()
                .sorted(Comparator.comparing(TopShareDto::getPercentOfGrowth).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

}

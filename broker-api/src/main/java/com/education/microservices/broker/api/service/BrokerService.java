package com.education.microservices.broker.api.service;

import com.education.microservices.broker.api.dto.ShareDto;
import com.education.microservices.broker.api.mapper.ShareMapper;
import com.education.microservices.broker.api.model.ShareWithReference;
import com.education.microservices.broker.api.repository.ShareRepository;
import com.education.microservices.broker.api.repository.ShareWithReferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.contract.v1.Share;
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

}

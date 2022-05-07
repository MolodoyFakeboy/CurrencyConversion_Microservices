package com.education.microservices.broker.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "HistoricCandle")
public class Candle {

    @Id
    private String id;

    private BigDecimal openPrice;

    private BigDecimal closePrice;

    private BigDecimal highDailyPrice;

    private BigDecimal lowestDailyPrice;

    private String time;
}

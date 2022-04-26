package com.education.microservices.broker.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShareDto {

    private String figi;
    private String fullName;
    private String shortName;
    private String country;
    private BigDecimal openPrice;
    private BigDecimal closePrice;
    private BigDecimal highDailyPrice;
    private BigDecimal lowestDailyPrice;
    private String time;
}

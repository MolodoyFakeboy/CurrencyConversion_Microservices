package com.education.microservices.broker.api.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TopShareDto {

    private String shortName;
    private BigDecimal percentOfGrowth;
}

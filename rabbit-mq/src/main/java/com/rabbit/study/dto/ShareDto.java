package com.rabbit.study.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShareDto {

    private String figi;
    private String fullName;
    private String shortName;
    private String country;
    private double openPrice;
    private double closePrice;
    private double highDailyPrice;
    private double lowestDailyPrice;
    private String time;
}

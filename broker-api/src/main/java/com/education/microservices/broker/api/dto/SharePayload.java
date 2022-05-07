package com.education.microservices.broker.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.contract.v1.Share;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SharePayload {

    private Share share;
    private String stockFigi;
    private HistoricCandle historicCandle;
}

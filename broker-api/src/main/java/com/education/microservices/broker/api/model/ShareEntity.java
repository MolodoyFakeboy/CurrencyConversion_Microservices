package com.education.microservices.broker.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "share")
public class ShareEntity {

    @Id
    @Field("_id")
    private String id;

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

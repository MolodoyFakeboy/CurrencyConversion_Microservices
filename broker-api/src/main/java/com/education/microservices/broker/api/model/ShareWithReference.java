package com.education.microservices.broker.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "shareWithReference")
public class ShareWithReference {

    @Id
    private String id;

    private String figi;

    private String fullName;

    private String shortName;

    private String country;

    @DBRef
    private List<Candle> candles;

}

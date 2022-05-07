package com.education.microservices.broker.api.repository;

import com.education.microservices.broker.api.model.Candle;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CandleRepository extends MongoRepository<Candle, String> {

}

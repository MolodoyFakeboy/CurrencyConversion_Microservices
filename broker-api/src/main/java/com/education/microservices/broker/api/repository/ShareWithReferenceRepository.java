package com.education.microservices.broker.api.repository;

import com.education.microservices.broker.api.model.ShareWithReference;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;


public interface ShareWithReferenceRepository extends MongoRepository<ShareWithReference, String> {

    ShareWithReference getByShortName(String shortName);

    @Query("{ 'candles.id': ?0 }")
    Optional<ShareWithReference> getByCandleId(String candleId);
}

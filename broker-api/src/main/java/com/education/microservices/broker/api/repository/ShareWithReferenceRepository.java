package com.education.microservices.broker.api.repository;

import com.education.microservices.broker.api.model.ShareWithReference;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShareWithReferenceRepository extends MongoRepository<ShareWithReference, String> {

    ShareWithReference getByShortName(String shortName);
}

package com.education.microservices.broker.api.repository;

import com.education.microservices.broker.api.model.ShareEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ShareRepository extends MongoRepository<ShareEntity, String> {

    List<ShareEntity> findByShortName(String name);

    List<ShareEntity> findByTimeRegex(String time);

    @Query("{shortName : ?0 , time : { $regex : ?1 , $options: 'i'}}")
    List<ShareEntity> findByNameAndTime(String name, String time);

}

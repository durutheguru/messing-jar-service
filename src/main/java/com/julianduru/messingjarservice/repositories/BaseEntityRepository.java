package com.julianduru.messingjarservice.repositories;

import com.julianduru.messingjarservice.entities.BaseEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * created by julian on 27/08/2022
 */
public interface BaseEntityRepository<T extends BaseEntity> extends ReactiveMongoRepository<T, String> {


    Mono<T> findByCode(String code);


}


package com.julianduru.messingjarservice.modules.kafka;

import com.julianduru.messingjarservice.BaseEntityRepository;
import com.julianduru.messingjarservice.entities.QueueIncomingMessage;
import com.julianduru.queueintegrationlib.model.IncomingMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * created by Julian Duru on 02/03/2023
 */
@Repository
public interface QueueIncomingMessageRepository extends BaseEntityRepository<QueueIncomingMessage> {



    Mono<Boolean> existsByReference(String reference);


    Mono<Boolean> existsByStatus(IncomingMessage.Status status);


    Flux<QueueIncomingMessage> findByStatus(IncomingMessage.Status status, Pageable pageable);



}

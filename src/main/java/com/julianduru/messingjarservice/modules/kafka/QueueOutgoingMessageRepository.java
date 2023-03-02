package com.julianduru.messingjarservice.modules.kafka;

import com.julianduru.messingjarservice.BaseEntityRepository;
import com.julianduru.messingjarservice.entities.QueueOutgoingMessage;
import com.julianduru.queueintegrationlib.model.OutgoingMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * created by Julian Duru on 02/03/2023
 */
@Repository
public interface QueueOutgoingMessageRepository extends BaseEntityRepository<QueueOutgoingMessage> {


    Mono<Boolean> existsByReference(String reference);

    Mono<Boolean> existsByStatus(OutgoingMessage.Status status);

    Mono<Boolean> existsByReferenceAndStatus(String reference, OutgoingMessage.Status status);

    Mono<QueueOutgoingMessage> findByReference(String reference);

    Mono<QueueOutgoingMessage> findByReferenceAndStatus(String reference, OutgoingMessage.Status status);

    Flux<QueueOutgoingMessage> findByStatus(OutgoingMessage.Status status, Pageable pageable);



}

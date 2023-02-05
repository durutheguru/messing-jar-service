package com.julianduru.messingjarservice.repositories;

import com.julianduru.messingjarservice.entities.ChatMessage;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * created by julian on 24/01/2023
 */
@Repository
public interface ChatMessageRepository extends BaseEntityRepository<ChatMessage> {


    Flux<ChatMessage> findByChatId(ObjectId chatId);


    Flux<ChatMessage> findByChatId(ObjectId chatId, Pageable pageable);


    Mono<ChatMessage> findFirstByChatIdOrderByCreatedDateDesc(ObjectId chatId);


}


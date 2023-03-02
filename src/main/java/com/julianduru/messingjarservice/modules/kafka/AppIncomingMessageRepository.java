package com.julianduru.messingjarservice.modules.kafka;

import com.julianduru.messingjarservice.entities.QueueIncomingMessage;
import com.julianduru.queueintegrationlib.model.IncomingMessage;
import com.julianduru.queueintegrationlib.module.subscribe.repo.IncomingMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * created by Julian Duru on 02/03/2023
 */
@Component
@RequiredArgsConstructor
public class AppIncomingMessageRepository implements IncomingMessageRepository {


    private final QueueIncomingMessageRepository incomingMessageRepository;



    @Override
    public IncomingMessage save(IncomingMessage message) {
        return incomingMessageRepository.save(QueueIncomingMessage.from(message))
            .toFuture().join()
            .toIncomingMessage();
    }


    @Override
    public List<IncomingMessage> saveAll(List<IncomingMessage> messages) {
        return messages.stream().map(this::save).toList();
    }


    @Override
    public boolean existsByReference(String reference) {
        return incomingMessageRepository.existsByReference(reference).toFuture().join();
    }


    @Override
    public boolean existsByStatus(IncomingMessage.Status status) {
        return incomingMessageRepository.existsByStatus(status).toFuture().join();
    }


    @Override
    public Page<IncomingMessage> findByStatus(IncomingMessage.Status status, Pageable pageable) {
        return new PageImpl<>(
            incomingMessageRepository.findByStatus(status, pageable).collectList().toFuture().join()
                .stream().map(QueueIncomingMessage::toIncomingMessage).toList()
        );
    }



}

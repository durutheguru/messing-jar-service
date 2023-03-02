package com.julianduru.messingjarservice.modules.kafka;

import com.julianduru.messingjarservice.entities.QueueOutgoingMessage;
import com.julianduru.queueintegrationlib.model.OutgoingMessage;
import com.julianduru.queueintegrationlib.module.publish.repo.OutgoingMessageRepository;
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
public class AppOutgoingMessageRepository implements OutgoingMessageRepository {


    private final QueueOutgoingMessageRepository outgoingMessageRepository;



    @Override
    public OutgoingMessage save(OutgoingMessage message) {
        return outgoingMessageRepository.save(QueueOutgoingMessage.from(message))
            .toFuture().join()
            .toOutgoingMessage();
    }


    @Override
    public List<OutgoingMessage> saveAll(List<OutgoingMessage> messages) {
        return messages.stream().map(this::save).toList();
    }


    @Override
    public boolean existsByReference(String reference) {
        return outgoingMessageRepository.existsByReference(reference).toFuture().join();
    }


    @Override
    public boolean existsByReferenceAndStatus(String reference, OutgoingMessage.Status status) {
        return outgoingMessageRepository.existsByReferenceAndStatus(reference, status).toFuture().join();
    }


    @Override
    public boolean existsByStatus(OutgoingMessage.Status status) {
        return outgoingMessageRepository.existsByStatus(status).toFuture().join();
    }

    @Override
    public Page<OutgoingMessage> findByStatus(OutgoingMessage.Status status, Pageable pageable) {
        return new PageImpl<>(
            outgoingMessageRepository.findByStatus(status, pageable).collectList().toFuture().join()
                .stream().map(QueueOutgoingMessage::toOutgoingMessage).toList()
        );
    }


}

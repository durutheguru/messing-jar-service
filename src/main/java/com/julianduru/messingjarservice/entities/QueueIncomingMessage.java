package com.julianduru.messingjarservice.entities;

import com.julianduru.queueintegrationlib.model.IncomingMessage;
import com.julianduru.queueintegrationlib.model.OperationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * created by Julian Duru on 02/03/2023
 */
@Data
@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueIncomingMessage extends BaseEntity {


    @Indexed(unique = true)
    private String reference;


    private String topic;


    private String header;


    private String payload;


    private IncomingMessage.Status status;


    private OperationStatus processingStatus;


    public static QueueIncomingMessage from(IncomingMessage message) {
        var msg = QueueIncomingMessage.builder()
                .reference(message.getReference())
                .topic(message.getTopic())
                .header(message.getHeader())
                .payload(message.getPayload())
                .status(message.getStatus())
                .processingStatus(message.getProcessingStatus())
                .build();

        if (message.getId() != null) {
            msg.setId(new ObjectId(message.getId()));
        }

        return msg;
    }


    public IncomingMessage toIncomingMessage() {
        var msg = IncomingMessage.builder()
                .reference(getReference())
                .topic(getTopic())
                .header(getHeader())
                .payload(getPayload())
                .status(getStatus())
                .processingStatus(getProcessingStatus())
                .build();

        if (getId() != null) {
            msg.setId(getIdString());
        }

        return msg;
    }



}


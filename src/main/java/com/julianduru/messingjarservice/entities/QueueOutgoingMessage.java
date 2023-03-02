package com.julianduru.messingjarservice.entities;

import com.julianduru.queueintegrationlib.model.IncomingMessage;
import com.julianduru.queueintegrationlib.model.OperationStatus;
import com.julianduru.queueintegrationlib.model.OutgoingMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * created by Julian Duru on 02/03/2023
 */
@Data
@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueueOutgoingMessage extends BaseEntity {


    @Indexed(unique = true)
    private String reference;


    private String topic;


    private String header;


    private String payload;


    private OutgoingMessage.Status status;


    private OperationStatus processingStatus;


    public static QueueOutgoingMessage from(OutgoingMessage message) {
        var msg = QueueOutgoingMessage.builder()
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


    public OutgoingMessage toOutgoingMessage() {
        var msg = OutgoingMessage.builder()
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

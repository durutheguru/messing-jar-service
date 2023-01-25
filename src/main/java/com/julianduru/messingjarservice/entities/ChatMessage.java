package com.julianduru.messingjarservice.entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/**
 * created by julian on 24/01/2023
 */
@Data
@Document
public class ChatMessage extends BaseEntity {


    @NotNull(message = "Chat ID required on Chat Message")
    private ObjectId chatId;


    @NotNull(message = "From User required on Chat Message")
    private ObjectId fromUserId;


    @NotNull(message = "To User required on Chat Message")
    private ObjectId toUserId;


    @NotEmpty(message = "Chat Message cannot be empty")
    private String message;


    private boolean received;


    private ZonedDateTime dateReceived;


    private boolean viewed;


    private ZonedDateTime dateViewed;


}



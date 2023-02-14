package com.julianduru.messingjarservice.entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * created by julian on 14/02/2023
 */
@Data
@Document
public class GroupMessage extends BaseEntity {


    @NotNull(message = "Group ID required on Group Message")
    private ObjectId groupId;


    @NotNull(message = "From User required on Group Message")
    private ObjectId fromUserId;


    @NotEmpty(message = "Chat Message cannot be empty")
    private String message;


    @NotNull(message = "Chat Message Type is required")
    private ChatMessageType type;


}

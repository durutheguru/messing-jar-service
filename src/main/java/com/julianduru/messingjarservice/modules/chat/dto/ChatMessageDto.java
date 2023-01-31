package com.julianduru.messingjarservice.modules.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.julianduru.messingjarservice.entities.ChatMessage;
import com.julianduru.messingjarservice.entities.ChatMessageType;
import com.julianduru.messingjarservice.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * created by julian on 25/01/2023
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {


    private String id;


    private String from;


    private String to;


    private String message;


    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private ChatMessageType type;


    private ZonedDateTime timeSent;



    public static ChatMessageDto from(ChatMessage msg) {
        return ChatMessageDto.builder()
            .id(msg.getIdString())
            .from(msg.getFromUserId().toString())
            .to(msg.getToUserId().toString())
            .message(msg.getMessage())
            .timeSent(
                msg.getCreatedDate() != null ?
                    ZonedDateTime.of(msg.getCreatedDate(), ZoneId.systemDefault()) :
                    null
            )
            .type(msg.getType())
            .build();
    }


    public static ChatMessageDto from(ChatMessage msg, User sender, User receiver) {
        return from(msg, sender.getUsername(), receiver.getUsername());
    }


    public static ChatMessageDto from(ChatMessage msg, String senderUsername, String receiverUsername) {
        return ChatMessageDto.builder()
            .id(msg.getIdString())
            .from(senderUsername)
            .to(receiverUsername)
            .message(msg.getMessage())
            .type(msg.getType())
            .timeSent(
                msg.getCreatedDate() != null ?
                    ZonedDateTime.of(msg.getCreatedDate(), ZoneId.systemDefault()) :
                    null
            )
            .build();
    }


}



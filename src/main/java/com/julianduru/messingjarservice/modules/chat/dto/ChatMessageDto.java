package com.julianduru.messingjarservice.modules.chat.dto;

import com.julianduru.messingjarservice.entities.ChatMessage;
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
            .build();
    }


    public static ChatMessageDto from(ChatMessage msg, User sender, User receiver) {
        return ChatMessageDto.builder()
            .id(msg.getIdString())
            .from(sender.getUsername())
            .to(receiver.getUsername())
            .message(msg.getMessage())
            .timeSent(
                msg.getCreatedDate() != null ?
                    ZonedDateTime.of(msg.getCreatedDate(), ZoneId.systemDefault()) :
                    null
            )
            .build();
    }


}



package com.julianduru.messingjarservice.modules.group.dto;

import com.julianduru.messingjarservice.entities.ChatMessageType;
import com.julianduru.messingjarservice.entities.GroupMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * created by Julian Duru on 09/03/2023
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMessageDto {


    private String id;


    private String from;


    private String message;


    private ChatMessageType type;


    private ZonedDateTime timeSent;



    public static GroupMessageDto from(GroupMessage msg) {
        return GroupMessageDto.builder()
            .id(msg.getIdString())
            .from(msg.getFromUserId().toString())
            .message(msg.getMessage())
            .timeSent(
                msg.getCreatedDate() != null ?
                    ZonedDateTime.of(msg.getCreatedDate(), ZoneId.systemDefault()) :
                    null
            )
            .type(msg.getType())
            .build();
    }



}
